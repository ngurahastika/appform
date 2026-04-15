package com.example.formapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.formapp.utils.SnowflakeIdGenerator;


@Configuration
public class SnowflakeConfig {

    // You can set these in application.properties or use defaults
    @Value("${snowflake.worker-id:1}")
    private Long workerId;

    @Value("${snowflake.datacenter-id:1}")
    private Long datacenterId;

    @Bean
	public SnowflakeIdGenerator snowflakeIdGenerator() {
        return new SnowflakeIdGenerator(workerId, datacenterId);
    }
}