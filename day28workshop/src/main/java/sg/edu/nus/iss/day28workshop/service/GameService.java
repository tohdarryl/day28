package sg.edu.nus.iss.day28workshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.day28workshop.model.Game;
import sg.edu.nus.iss.day28workshop.repository.GameRepo;

@Service
public class GameService {
    
    @Autowired
    GameRepo gameRepo;

    public List<Game> getGameById(Integer gid){
        return gameRepo.getGameById(gid).stream()   
        .map(g -> Game.create(g))
        .toList();
    }
}
