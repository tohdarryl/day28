package sg.edu.nus.iss.day28workshop.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.day28workshop.model.EditedReview;
import sg.edu.nus.iss.day28workshop.model.Game;
import sg.edu.nus.iss.day28workshop.model.Review;
import sg.edu.nus.iss.day28workshop.repository.ReviewRepo;

@Service
public class ReviewService {
    
    @Autowired
    ReviewRepo reviewRepo;


    public String addReview(Review review) {
        String reviewId = UUID.randomUUID().toString().substring(0, 8);
        review.setRId(reviewId);
        reviewRepo.insertReview(review);
        return reviewId;
    }


    public Review updateReviewById(String rId, String json) throws IOException{
            return reviewRepo.updateReviewById(rId, json);
    }


    public Review getReviewById(String _id){
    
        Review r = reviewRepo.getReviewById(_id);
        
        if(r.getEdited() != null){
            List<EditedReview> ll = (List<EditedReview>) r.getEdited();
            System.out.println(ll.size());
            // if there is an edited review in list, then Boolean = true
            if (ll.size() > 0)
                r.setIsEdited(Boolean.valueOf(true));
            else
                r.setIsEdited(Boolean.valueOf(false));
        }
        r.setTimestamp(LocalDateTime.now());

        return r;
    }

    //Day28Workshop #b
    public Optional<Game> aggregateGame(String gid){
        return reviewRepo.aggregateGameReviews(gid);
    }
    
    public List<Review> aggregateGamesComments(Integer limit, String username, Integer rating){
            return reviewRepo.aggregateGamesComments(limit, username, rating);
    }

}
