package io.github.pianpu.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@ComponentScan(basePackages = "io.github.pianpu.security")
@Configuration
public class ScanConfig {
}
