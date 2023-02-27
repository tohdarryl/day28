package sg.edu.nus.iss.day28.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;
import static sg.edu.nus.iss.day28.Constants.*; 

@Repository
public class CommentRepo {
    @Autowired
    MongoTemplate template;

    public List<Document> searchCommentText(String... texts){

        for (int i = 0; i < texts.length; i++) 
			System.out.printf(">>> texts[%d]: %s\n", i, texts[i]);

        //Create critera for text matching
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                                .matchingAny(texts);

        TextQuery textQuery = TextQuery.queryText(criteria)
                        .includeScore(FIELD_TEXT_SCORE)
                        .sortByScore();
                        
        // Returns only 3 items
        Query query = textQuery.limit(3);
        return template.find(query, Document.class, COLLECTION_COMMENTS);
    }
    
}
