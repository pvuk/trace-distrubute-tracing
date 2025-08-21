package com.nl.trace.distribute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import zipkin2.server.internal.EnableZipkinServer;
/**
 * 
 * @author Venkata.Pulipati
 * @since Wednesday 30-July-2025 10:20:12
 */
@SpringBootApplication
@EnableZipkinServer
public class TraceDistrubuteTracingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TraceDistrubuteTracingApplication.class, args);
	}

}
