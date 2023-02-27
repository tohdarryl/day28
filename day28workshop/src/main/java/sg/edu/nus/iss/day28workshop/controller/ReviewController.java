package sg.edu.nus.iss.day28workshop.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.day28workshop.model.Game;
import sg.edu.nus.iss.day28workshop.model.Review;
import sg.edu.nus.iss.day28workshop.service.GameService;
import sg.edu.nus.iss.day28workshop.service.ReviewService;

@Controller
@RequestMapping({"","index.html"})
public class ReviewController {
    
    @Autowired
    ReviewService reviewSvc;
    
    @Autowired
    GameService gameSvc;

    @PostMapping("/review")
    public String postComment(@RequestBody MultiValueMap<String, String> form, Model model){
        Review r = Review.create(form);
        System.out.println(r);

        List<Game> results = gameSvc.getGameById(Integer.parseInt(form.getFirst("gameId")));
        Game g = results.get(0);
        
        if(g.getName() == null){
            return "index";
        }

        r.setName(g.getName());
        r.setPosted(LocalDateTime.now());
		String reviewId = reviewSvc.addReview(r);
		System.out.printf(">>>> reviewId: %s\n", reviewId);
        
        System.out.println(r);
        
        model.addAttribute("r", r);
		// return "redirect:/";
        return "insert";
    }
}
