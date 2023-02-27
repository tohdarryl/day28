package sg.edu.nus.iss.day28workshop.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditedReview {
    private String comment;
    private Integer rating;
    private LocalDateTime posted;


    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("comment", this.getComment())
                .add("rating", this.getRating())
                .add("posted", this.getPosted().toString())
                .build();
    }

    // to convert from String to JsonObject to set our Order Object attributes
    public static EditedReview createEditedReview(String json) throws IOException{
        EditedReview eR = new EditedReview();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            eR.setComment(o.getString("comment"));
            eR.setRating(o.getInt("rating"));
            eR.setPosted(LocalDateTime.now());
    }

    return eR;
    }

    public JsonObjectBuilder toJSONTaskC() {
        return Json.createObjectBuilder()
                .add("comment", this.getComment())
                .add("rating", this.getRating())
                .add("posted", this.getPosted().toString());
    }
}
