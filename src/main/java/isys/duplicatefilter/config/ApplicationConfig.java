package isys.duplicatefilter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"isys.duplicatefilter.repositories"})
public class ApplicationConfig {

}

