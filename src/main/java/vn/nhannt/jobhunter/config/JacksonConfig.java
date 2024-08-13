package vn.nhannt.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import vn.nhannt.jobhunter.util.constant.ApiMethodDeserializer;
import vn.nhannt.jobhunter.util.constant.ApiMethodEnum;
import vn.nhannt.jobhunter.util.constant.ModuleDeserializer;
import vn.nhannt.jobhunter.util.constant.ModuleEnum;

@Configuration
public class JacksonConfig {
    @Bean
    public Module customEnumModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ApiMethodEnum.class, new ApiMethodDeserializer());
        module.addDeserializer(ModuleEnum.class, new ModuleDeserializer());
        return module;
    }
}
