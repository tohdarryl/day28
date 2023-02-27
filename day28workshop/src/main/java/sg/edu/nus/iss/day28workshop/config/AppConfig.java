package sg.edu.nus.iss.day28workshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import static sg.edu.nus.iss.day28workshop.Constants.*;

@Configuration
public class AppConfig {
        // Inject the mongo connection String
        @Value("${mongo.url}")
        private String mongoUrl;
    
        @Bean
        public MongoTemplate createTemplate() {
            MongoClient client = MongoClients.create(mongoUrl);
            return new MongoTemplate(client, DATABASE_BGG);
        }
}
