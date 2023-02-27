package sg.edu.nus.iss.day28.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import static sg.edu.nus.iss.day28.Constants.*;

@Repository
public class TvShowRepo {
    @Autowired
    @Qualifier("shows")
    MongoTemplate template;

    /*
     * db.tv.aggregate([
     * {
     * $match {
     * language: {$regex: 'english', $options: 'i'}
     * }
     * },
     * {
     * $project:{
     * _id:0, name:1, url:1, genres:1}
     * },
     * {
     * $limit:3
     * }
     * ])
     */
    public List<Document> find() {
        Criteria criteria = Criteria.where("language").regex("english", "i");

        // Find or filter docs: $match
        MatchOperation matchLang = Aggregation.match(criteria);

        // $project
        ProjectionOperation project = Aggregation.project()
                .andInclude("url", "genres", "name")
                .andExclude("_id");

        // Specify a limit: $limit
        LimitOperation limitResult = Aggregation.limit(3);

        Aggregation pipeline = Aggregation.newAggregation(matchLang, project, limitResult);

        AggregationResults<Document> results = template.aggregate(pipeline, "tv", Document.class);

        // to convert results to a list
        return results.getMappedResults();
    }

    /*
     * db.tv.aggregate([
     * {
     * $group: {
     * // '$' infront of 'runtime', to group data by values of 'runtime'
     * _id:"$runtime",
     * shows: {
     * $push:{
     * title: "$name",
     * language:"$language"
     * }
     * },
     * 
     * // count the number of object
     * total:{ $sum:1},
     * // average rating
     * avgRating: { $avg:"$rating.average"}
     * }
     * },
     * 
     * { // filter for r_id 'runtime' value = 25
     * $match:{
     * _id: {$eq: 25}
     * }
     * }
     * ]);
     */
    public List<Document> groupTvShowsByRuntime() {

        String push = """
                title: "$name",
                language: "$language"
                    """;

        GroupOperation groupRuntime = Aggregation.group("runtime")
                // .push(push).as("shows")
                .push("name").as("title")
                .push("language").as("language")
                // for average rating calculation
                .avg("rating.average").as("averageRating")
                .count().as("total");
        // For $match
        MatchOperation matchRuntime = Aggregation.match(Criteria.where("_id").is(50));

        Aggregation pipeline = Aggregation.newAggregation(groupRuntime, matchRuntime);

        return template.aggregate(pipeline, COLLECTION_TV, Document.class)
                .getMappedResults();
    }

    /*
     * db.tv.aggregate([
     * {
     * $project: {
     * title: { $concat: ["$name", " (", { $toString: "$runtime"}, ")"]
     * },
     * rating: "$rating.average"
     * }
     * }
     * ]);
     */

    public List<Document> getTitleAndRating() {
        ProjectionOperation project = Aggregation.project()
                // .and(FIELD_NAME).as(FIELD_TITLE)
                .and(
                        AggregationExpression.from(
                                MongoExpression.create("""
                                    $concat: [ "$name", " (", { $toString: "$runtime" }, ")"]        
                                """)))
                        // StringOperators.Concat.valueOf("name").concat(" (")
                        //         .concatValueOf("runtime").concat(")"))
                .as("title")
                .and("rating.average").as("rating")
                .andExclude("_id");

        Aggregation pipeline = Aggregation.newAggregation(project);

        return template.aggregate(pipeline, COLLECTION_TV, Document.class)
                .getMappedResults();
    }
}
