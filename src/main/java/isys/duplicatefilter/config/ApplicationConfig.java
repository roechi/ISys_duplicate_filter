package isys.duplicatefilter.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"isys.duplicatefilter.repositories"})
public class ApplicationConfig extends AbstractMongoConfiguration {


    @Value("${db.uri}")
    private String dbUri;

    @Value(("${db.host}"))
    private String dbHost;

    @Value("${db.name}")
    private String dbName;

    @Value("${db.port}")
    private int dbPort;

    @Override
    protected String getDatabaseName() {
        if (0 != "none".compareTo(dbUri)) {
            int startOfDbName = dbUri.lastIndexOf("/");
            this.dbName = dbUri.substring(startOfDbName + 1);
        }
        return dbName;
    }

    @Override
    public Mongo mongo() throws Exception {
        MongoClient mongoClient;
        if ("none".compareTo(dbUri) != 0) {
            MongoClientURI uri = new MongoClientURI(dbUri);
            mongoClient = new MongoClient(uri);
        } else {
            mongoClient = new MongoClient(dbHost, dbPort);
        }
        return mongoClient;
    }
}

