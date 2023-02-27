package sg.edu.nus.iss.day28;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sg.edu.nus.iss.day28.repository.CommentRepo;
import sg.edu.nus.iss.day28.repository.TvShowRepo;

@SpringBootApplication
public class Day28Application implements CommandLineRunner{
	
	@Autowired
	CommentRepo commentRepo;

	@Autowired
	TvShowRepo tvShowRepo;

	public static void main(String[] args) {
		SpringApplication.run(Day28Application.class, args);
	}

	@Override
	public void run(String... args){
		// List<Document> results = commentRepo.searchCommentText("enjoyed", "loved", "super");
		//List<Document> results = tvShowRepo.find();
		List<Document> results = tvShowRepo.groupTvShowsByRuntime();
		// List<Document> results = tvShowRepo.getTitleAndRating();

		for(Document d : results)
		System.out.printf(">>> %s\n", d.toJson());
	}

}
