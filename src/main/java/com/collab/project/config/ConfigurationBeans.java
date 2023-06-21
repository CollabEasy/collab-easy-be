package com.collab.project.config;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationBeans {

    @Bean
    public GsonFactory gsonFactory() {
        return GsonFactory.getDefaultInstance();
    }


    @Bean
    public MemoryDataStoreFactory memoryDataStoreFactory() {
        return MemoryDataStoreFactory.getDefaultInstance();
    }
}
