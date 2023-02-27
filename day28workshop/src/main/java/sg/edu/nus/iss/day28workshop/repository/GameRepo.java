package sg.edu.nus.iss.day28workshop.repository;

import static sg.edu.nus.iss.day28workshop.Constants.*;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepo {
    @Autowired
    MongoTemplate template;

    public List<Document> getGameById(Integer gid){
        Criteria c = Criteria.where(FIELD_GID).in(gid);

        Query query = Query.query(c);

        return template.find(query, Document.class, COLLECTION_GAMES);
    }
}
