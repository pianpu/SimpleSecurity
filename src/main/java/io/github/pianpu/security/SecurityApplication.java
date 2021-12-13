 package io.github.pianpu.security;

 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.boot.SpringApplication;
 import org.springframework.boot.autoconfigure.SpringBootApplication;
 import org.springframework.context.ConfigurableApplicationContext;


 @SpringBootApplication
 public class SecurityApplication {

     static Logger logger = LoggerFactory.getLogger(SecurityApplication.class);

     public static void main(String[] args) {
         ConfigurableApplicationContext run = SpringApplication.run(SecurityApplication.class, args);
         // String[] beanDefinitionNames = run.getBeanDefinitionNames();
         // Stream<String> stream = Arrays.stream(beanDefinitionNames);
         // stream.forEach(name -> logger.info("bean =>"  + name));
     }

 }
