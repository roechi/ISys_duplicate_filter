package isys.duplicatefilter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@EnableMongoRepositories(basePackages = {"isys.duplicatefilter.repositories"})
public class ApplicationConfig {

    @Value("${article.repository.host}")
    private String articleRepositoryHost;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public String articleRepositoryHost() {
        return articleRepositoryHost;
    }
}

