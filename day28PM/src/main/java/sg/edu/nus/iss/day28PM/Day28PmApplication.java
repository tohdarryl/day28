package sg.edu.nus.iss.day28PM;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sg.edu.nus.iss.day28PM.repository.PStoreRepo;

@SpringBootApplication
public class Day28PmApplication implements CommandLineRunner{

	@Autowired
	PStoreRepo pStoreRepo;
	public static void main(String[] args) {
		SpringApplication.run(Day28PmApplication.class, args);
	}

	@Override
	public void run(String... args){
		List<Document> results = pStoreRepo.groupAppsByCategory2();

		for(Document d : results)
		System.out.printf(">>> %s\n", d.toJson());
	}
}
