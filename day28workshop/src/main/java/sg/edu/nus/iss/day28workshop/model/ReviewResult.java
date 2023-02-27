package sg.edu.nus.iss.day28workshop.model;

import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResult {
    private String rating;
    private String timestamp;
    private List<Review> games;


    public JsonObject toJSON() {

        return Json.createObjectBuilder()
                .add("rating", getRating())
                .add("games", getGames().toString())
                .add("timestamp", getTimestamp())
                .build();
    }
}
