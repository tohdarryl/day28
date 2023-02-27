package sg.edu.nus.iss.day28PM.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import static sg.edu.nus.iss.day28PM.Constant.*;

@Repository
public class PStoreRepo {
    @Autowired
    MongoTemplate template;

    /*
     db.playstore.aggregate([
    {    // Must Filter first then Group
        $match:{
            Rating:{$ne: NaN}
        }
    },
    {
        $group: {
        _id:"$Category",
        rating: {
            $push:{
                rating: "$Rating"
            }
        }}
    }

    ]);
     */

     public List<Document> groupAppsByCategory(){
        Criteria criteria = Criteria.where("Rating").ne(Double.NaN);

        // Find or filter docs: $match
        MatchOperation matchLang = Aggregation.match(criteria);

        // // $project
        // ProjectionOperation project = Aggregation.project()
        //                                 .andInclude("url","genres", "name")
        //                                 .andExclude("_id");

        // Specify a limit: $limit
        // LimitOperation limitResult = Aggregation.limit(3);

		GroupOperation groupCategory = Aggregation.group(FIELD_CATEGORY)
				//.push(push).as(FIELD_SHOWS)
				.push("Rating").as("rating");
				// .count().as("total");


        Aggregation pipeline = Aggregation.newAggregation(matchLang, groupCategory);

        AggregationResults<Document> results =
        template.aggregate(pipeline, "playstore", Document.class);

        // to convert results to a list
        return results.getMappedResults();

     }
     /*
      db.playstore.aggregate([
    {    // Must Filter first then Group
        $match:{
            Rating:{$ne: NaN}
        }
    },
    {
        $group: {
        _id:"$Category",
        apps: { $push: "$App" },
        averageRating: {$avg: "$Rating"}
        }
    }

]);
      */
     public List<Document> groupAppsByCategory2(){
        Criteria criteria = Criteria.where("Rating").ne(Double.NaN);

        // Find or filter docs: $match
        MatchOperation matchLang = Aggregation.match(criteria);

		GroupOperation groupCategory2 = Aggregation.group(FIELD_CATEGORY)
				//.push(push).as(FIELD_SHOWS)
				.push("App").as("app")
                .avg("Rating").as("averageRating");

        Aggregation pipeline = Aggregation.newAggregation(matchLang, groupCategory2);

        AggregationResults<Document> results =
        template.aggregate(pipeline, "playstore", Document.class);

        // to convert results to a list
        return results.getMappedResults();

     }

}
