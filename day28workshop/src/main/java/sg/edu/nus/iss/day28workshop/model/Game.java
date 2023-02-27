package sg.edu.nus.iss.day28workshop.model;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private Integer gid;
    private String name;
    private Integer year;
    private Integer ranking;
    private Integer usersRated;
    private String url;
    private String image;
    private String timestamp;
    private String[] reviews;


    public static Game create(Document d) {
        Game g = new Game();
        List reviewsIdArr = (ArrayList) d.get("reviews");
        List newReviewsId = new LinkedList<>();
        for (Object a : reviewsIdArr) {
            // to take ('<Id>') from ObjectId("63f4e17b35cb9612b4f85534")
            ObjectId oa = (ObjectId) a;
            newReviewsId.add("/review/" + oa.toString());
        }
        g.setGid(d.getInteger("gid"));
        g.setName(d.getString("name"));
        g.setYear(d.getInteger("year"));
        g.setRanking(d.getInteger("ranking"));
        g.setUsersRated(d.getInteger("users_rated"));
        g.setUrl(d.getString("url"));
        g.setImage(d.getString("image"));
        g.setTimestamp(d.getDate("timestamp").toString());
        g.setReviews((String[]) newReviewsId.toArray(new String[newReviewsId.size()]));

        return g;

        
    }

    public JsonObject toJSON() {
        JsonArray reviews = null;
        JsonArrayBuilder bld = Json.createArrayBuilder();
        for (String x : getReviews())
            bld.add(x);
        //Built JsonArray 'reviews'
        reviews = bld.build();

        return Json.createObjectBuilder()
                .add("gid", getGid())
                .add("name", getName())
                .add("year", getYear())
                .add("ranking", getRanking())
                .add("users_rated", getUsersRated() != null ? getUsersRated() : 0)
                .add("url", getUrl())
                .add("image", getImage())
                .add("timestamp", getTimestamp())
                .add("reviews", reviews)
                .build();
    }

}
