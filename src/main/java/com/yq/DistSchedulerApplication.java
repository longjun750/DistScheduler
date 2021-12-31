package com.yq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Simple to Introduction
 * className: LogbackApplication
 *
 * @author EricYang
 * @version 2018/11/10 11:14
 */
@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class DistSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DistSchedulerApplication.class, args);
        log.info("DistSchedulerApplication Start done.");
    }
}
