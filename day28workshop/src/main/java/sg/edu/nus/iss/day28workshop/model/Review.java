package sg.edu.nus.iss.day28workshop.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.util.MultiValueMap;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private String rId;
    private String user;
    private Integer rating;
    private String comment;
    private Integer gid;
    private LocalDateTime posted;
    private String name;
    private List<EditedReview> edited = new LinkedList<>();
    private Boolean isEdited;
    private LocalDateTime timestamp;

    // Task a
    public Document toDocument() {
        Document doc = new Document();
        doc.put("rId", getRId());
        doc.put("user", getUser());
        doc.put("rating", getRating());
        doc.put("comment", getComment());
        doc.put("gid", getGid());
        doc.put("posted", getPosted());
        doc.put("name", getName());
        return doc;
    }

    // Task a
    public static Review create(MultiValueMap<String, String> form) {
        Review r = new Review();
        r.setUser(form.getFirst("user"));
        r.setRating(Integer.parseInt(form.getFirst("rating")));
        r.setComment(form.getFirst("comment"));
        r.setGid(Integer.parseInt(form.getFirst("gameId")));
        return r;
    }

    // Task b
    public static Review createB(Document d) {
        Review r = new Review();
        r.setRId(d.getString("rId"));
        r.setUser(d.getString("user"));
        r.setRating(d.getInteger("rating"));
        r.setComment(d.getString("comment"));
        r.setGid(d.getInteger("gid"));
        LocalDateTime postedDt = Instant.ofEpochMilli(d.getDate("posted").getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        r.setPosted(postedDt);
        r.setName(d.getString("name"));

        return r;
    }

    public JsonObject toJSONTaskB() {
        return Json.createObjectBuilder()
                .add("user", this.getUser())
                .add("rating", this.getRating())
                .add("comment", this.getComment())
                .add("gid", this.getGid())
                .add("posted", this.getPosted().toString())
                .add("name", this.getName())
                .add("edited", this.getIsEdited())
                .add("timestamp", this.getTimestamp().toString())
                .build();
    }

    public JsonObject toJSONTaskC() {
        // Converting from Java Object: List to JSON object: Array
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        List<JsonObjectBuilder> listOfEditedReviews = this.getEdited()
                .stream()
                // Using EditedReview.java JsonObjectBuilder toJSON method to build JsonObject for EditedReview
                .map(e -> e.toJSONTaskC())
                .toList();
        // Iterate and add into ArrayBuilder
        for (JsonObjectBuilder x : listOfEditedReviews)
            arrBuilder.add(x);
        return Json.createObjectBuilder()
                .add("user", this.getUser())
                .add("rating", this.getRating())
                .add("comment", this.getComment())
                .add("gid", this.getGid())
                .add("posted", this.getPosted().toString())
                .add("name", this.getName())
                .add("edited", arrBuilder)
                .add("timestamp", this.getTimestamp().toString())
                .build();
    }
}
