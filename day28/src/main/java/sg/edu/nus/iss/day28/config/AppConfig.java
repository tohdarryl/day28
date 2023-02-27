package sg.edu.nus.iss.day28.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class AppConfig {
        // Inject the mongo connection String
        @Value("${mongo.url}")
        private String mongoUrl;
        // Get source from 'BGG' Mongo database
        @Primary
        @Bean(name="bgg")
        public MongoTemplate createBGG() {
            MongoClient client = MongoClients.create(mongoUrl);
            return new MongoTemplate(client, "bgg");
        }
        // Get source from 'shows' Mongo database
        @Bean(name="shows")
        public MongoTemplate createShows() {
            MongoClient client = MongoClients.create(mongoUrl);
            return new MongoTemplate(client, "shows");
        }
}
