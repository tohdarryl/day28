package sg.edu.nus.iss.day28workshop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import sg.edu.nus.iss.day28workshop.model.EditedReview;
import sg.edu.nus.iss.day28workshop.model.Review;
import sg.edu.nus.iss.day28workshop.service.ReviewService;

@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE)
public class ReviewRestController {
    
    @Autowired
    ReviewService reviewSvc;
    // Task B
    @PutMapping("/review/{rId}")
    public ResponseEntity<String> updateDetails(@PathVariable String rId, @RequestBody String json) throws IOException{
        Review r = reviewSvc.updateReviewById(rId, json);
        EditedReview eR = r.getEdited().get(r.getEdited().size()-1);
        System.out.println(r);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(eR.toJSON().toString());
    }
    // Task C
    @GetMapping("/review/{_id}")
    public ResponseEntity<String> getReviewById(@PathVariable String _id){
        Review r = reviewSvc.getReviewById(_id);
        System.out.println(r.getEdited());
        return ResponseEntity
        .status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(r.toJSONTaskB().toString());
    }
    
    // Task D
    @GetMapping("/review/{_id}/history")
    public ResponseEntity<String> getReviewHistory(@PathVariable String _id){
        Review r = reviewSvc.getReviewById(_id);
        System.out.println(r.getEdited());
        return ResponseEntity
        .status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(r.toJSONTaskC().toString());
    }

}
