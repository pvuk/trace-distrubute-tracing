✅ 1. Run Zipkin as a Java Application
Zipkin is a Java-based application, so you can run it directly using the JAR file.

Steps:
Download the latest Zipkin JAR from Zipkin GitHub Releases.
Run it using:
java -jar zipkin-server-<version>.jar

This will start Zipkin on port 9411 by default.

✅ Check for Armeria Conflicts
Your logs show Armeria is serving on port 8080. If you're using Armeria for Zipkin, it might override the default port.

To fix this, explicitly set Armeria’s port to 9411:
armeria:
  ports:
    - port: 9411
      protocols: [http]

Armeria is a high-performance asynchronous web server used by Zipkin internally. Unlike Spring Boot’s embedded Tomcat or Jetty, Armeria manages its own server ports and bindings.

✅ 2. Embed Zipkin Server in a Spring Boot App
You can create a Spring Boot application that runs Zipkin as a service.

Steps:
1. Create a new Spring Boot project.
2. Add this dependency:
<dependency>
  <groupId>io.zipkin.java</groupId>
  <artifactId>zipkin-server</artifactId>
  <version>2.23.16</version>
</dependency>

3. Annotate your main class:
@SpringBootApplication
@EnableZipkinServer
public class ZipkinApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinApplication.class, args);
    }
}

4. Run the application and access Zipkin at http://localhost:9411.

To integrate Zipkin 3.5.1 into your existing Spring Boot microservices (trace-api-gateway, trace-bank, trace-cards, and trace-service-registry), follow these steps:

✅ 1. Add Zipkin Dependencies to Each Microservice
In each service’s pom.xml:
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>

Ensure your spring-cloud-dependencies BOM is included in dependencyManagement.

✅ 2. Configure application.yml or application.properties
In each microservice:
spring:
  zipkin:
    base-url: http://localhost:9411
    enabled: true
  sleuth:
    sampler:
      probability: 1.0

Adjust base-url if Zipkin is hosted elsewhere.

✅ 3. Run the Services
Start trace-service-registry
Start ZipkinApplication
Start trace-api-gateway, trace-bank, and trace-cards

✅ 4. Verify Traces
Visit http://localhost:9411 and trigger some API calls through trace-api-gateway. You should see traces appear in the Zipkin UI.

Fix Errors

The bean 'prometheusMeterRegistry', defined in class path resource [org/springframework/boot/actuate/autoconfigure/metrics/export/prometheus/PrometheusMetricsExportAutoConfiguration.class], could not be registered. A bean with that name has already been defined in zipkin2.server.internal.prometheus.ZipkinPrometheusMetricsConfiguration and overriding is disabled.

Action:

Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true

Solution:
This error occurs because Zipkin 3.5.1 already defines a prometheusMeterRegistry bean internally, and Spring Boot's Prometheus auto-configuration is trying to define another one. Since bean overriding is disabled by default, Spring throws this conflict.

✅ Solution Options
Option 1: Enable Bean Overriding
Add this to your application.yml or application.properties in the Zipkin server project:

spring:
  main:
    allow-bean-definition-overriding: true

This allows Spring to override the duplicate bean definition and proceed.

Option 2: Exclude Prometheus Auto-Configuration
If you don’t need Spring Boot’s Prometheus metrics (because Zipkin already provides them), you can exclude it:

@SpringBootApplication(exclude = {
    PrometheusMetricsExportAutoConfiguration.class
})
@EnableZipkinServer
public class ZipkinApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinApplication.class, args);
    }
}

This avoids loading the conflicting Prometheus configuration from Spring Boot.

✅ Recommendation
If you're using Zipkin's built-in Prometheus metrics and don't need Spring Boot's Prometheus integration, Option 2 is cleaner. Otherwise, Option 1 is simpler and works well for development.
--

