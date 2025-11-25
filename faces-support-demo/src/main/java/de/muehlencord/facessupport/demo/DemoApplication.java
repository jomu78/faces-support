package de.muehlencord.facessupport.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot starter application for demo project
 *
 * @author Joern Muehlencord, 2025-11-21
 * @since 0.1.0
 */
@SpringBootApplication
public class DemoApplication {

  /**
   * start the demo application
   *
   * @param args the application runtime arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }
}
