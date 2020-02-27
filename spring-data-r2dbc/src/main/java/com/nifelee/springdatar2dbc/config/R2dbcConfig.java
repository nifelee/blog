package com.nifelee.springdatar2dbc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.nifelee.springdatar2dbc.repository")
public class R2dbcConfig {

}
