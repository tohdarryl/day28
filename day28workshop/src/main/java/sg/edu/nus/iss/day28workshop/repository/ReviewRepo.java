package sg.edu.nus.iss.day28workshop.repository;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation.AddFieldsOperationBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day28workshop.model.EditedReview;
import sg.edu.nus.iss.day28workshop.model.Game;
import sg.edu.nus.iss.day28workshop.model.Review;

import static sg.edu.nus.iss.day28workshop.Constants.*;

import java.io.IOException;
import java.lang.StackWalker.Option;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Repository
public class ReviewRepo {
    
    @Autowired
    MongoTemplate template;

    // Task a
    public void insertReview(Review review){
        Document doc = review.toDocument();
        template.insert(doc, COLLECTION_REVIEWS);
    }
    // Task b
    public Review updateReviewById(String rId, String json) throws IOException{
        /* 
        ObjectId docId = new ObjectId(_id);
        Review r = template.findById(docId, Review.class, COLLECTION_REVIEWS);
        */
        
        Criteria c = Criteria.where(FIELD_RID).regex(rId,"i");
        Query q = Query.query(c);
        
        // Map data straight to Review.Class instead
        List<Review> list = template.find(q, Review.class, COLLECTION_REVIEWS);
        // List<Review> rList = list.stream()   
        //         .map(r -> Review.createB(r))
        //         .toList();
        Review r = list.get(0);
        
        if (r != null){
            EditedReview e = EditedReview.createEditedReview(json);
            // if List<EditedReview> not null, add EditedReview in;
            if(r.getEdited() != null){
            r.getEdited().add(e);
            // else create new list
            }else {
                List<EditedReview> ll = new LinkedList<>();
                ll.add(e);
                r.setEdited(ll);
            }

        Update updateOps = new Update()
            .push("edited").each(e);
            template.updateFirst(q,updateOps, COLLECTION_REVIEWS);

        }
        return r;
        
    }
    // Task C : use ObjectId to extract info
    public Review getReviewById(String _id){
        ObjectId docId = new ObjectId(_id);
        return template.findById(docId, Review.class, COLLECTION_REVIEWS);
    }

    /*
     db.games.aggregate([
// Filter for gid
    {
        $match: 
             {gid: 5}
        },
// Lookup for reviews
    {
        $lookup: 
             {
                 from: 'reviews',
                 foreignField: 'gid',
                 localField: 'gid',
                 as: 'reviews'
             }
        },        
// Select fields as required for task
    {
        $project:{
             _id:0, gid:1, name:1, year:1, ranking:1, users_rated:1,url:1, image:1, reviews: "$reviews._id", timestamp: "$$NOW"}
        },
]);
     */
    
    // day28workshop Task #a
    public Optional<Game> aggregateGameReviews(String gid){
        MatchOperation mo = Aggregation.match(
            Criteria.where("gid").is(Integer.parseInt(gid))
        );

        LookupOperation linkReviews = 
            Aggregation.lookup(COLLECTION_REVIEWS, "gid", "gid", "reviews");
        
        ProjectionOperation po = Aggregation.project()
                            .andInclude("gid", "name", "year", "ranking", "users_rated", "url", "image")
                            .and("reviews._id").as("reviews");

        AddFieldsOperationBuilder addFieldOpsBld = Aggregation.addFields();
        addFieldOpsBld.addFieldWithValue("timestamp", LocalDateTime.now());
        AddFieldsOperation newFieldOps = addFieldOpsBld.build();
        
        Aggregation pipeline = Aggregation.newAggregation(mo, linkReviews, po, newFieldOps);
        AggregationResults <Document> results = template.aggregate(pipeline, "games", Document.class);

        if (!results.iterator().hasNext())
            return Optional.empty();

        Document doc = results.iterator().next();
        Game g = Game.create(doc);

        return Optional.of(g);
    }

     // day28workshop Task #b
     public List<Review> aggregateGamesComments(Integer limit, String username, Integer rating){
            Criteria c = null;
            if (rating  > 5){
                c = new Criteria().andOperator(
                            Criteria.where("user").is(username),
                            Criteria.where("rating").gt(rating));
            } else {
                c = new Criteria().andOperator(
                            Criteria.where("user").is(username),
                            Criteria.where("rating").lt(rating));
            }

            MatchOperation matchUser = Aggregation.match(c);

            LookupOperation linkReviewGame = Aggregation.lookup("games",        
                        "gid", "gid", "gameReview");

            ProjectionOperation project = Aggregation.project()
                                            .andInclude("_id", "rId", "user", "rating",
                                            "comment", "gid")
                                            .and("gameReview.name").as("game_name");
            
            LimitOperation limitOps = Aggregation.limit(limit);

            Aggregation pipeline = Aggregation.newAggregation(matchUser, linkReviewGame, project, limitOps);

            AggregationResults<Review> results = template.aggregate(pipeline, "reviews", Review.class);

            return results.getMappedResults();

     }
}
