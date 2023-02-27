package sg.edu.nus.iss.day28PM.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import static sg.edu.nus.iss.day28PM.Constant.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class AppConfig {
     // Inject the mongo connection String
     @Value("${mongo.url}")
     private String mongoUrl;

     @Bean
        public MongoTemplate createBGG() {
            MongoClient client = MongoClients.create(mongoUrl);
            return new MongoTemplate(client, GOOGLEPLAYSTORE);
        }
}
