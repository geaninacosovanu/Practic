package rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configure {
    @Bean
    public String propFile()
    {
        return "Server/src/main/resources/server.properties";
    }
    @Bean
    public String conf()
    {
        return "Server/src/main/resources/hibernate.cfg.xml";
    }

}