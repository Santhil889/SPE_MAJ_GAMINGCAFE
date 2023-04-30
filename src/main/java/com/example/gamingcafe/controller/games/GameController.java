package com.example.gamingcafe.controller.games;

import ch.qos.logback.classic.Logger;
import com.example.gamingcafe.controller.gamer.GamerController;
import com.example.gamingcafe.model.auth.Creds;
import com.example.gamingcafe.model.gamer.GamerDetails;
import com.example.gamingcafe.model.games.Category;
import com.example.gamingcafe.model.games.GameDetails;
import com.example.gamingcafe.repo.auth.CredsRepo;
import com.example.gamingcafe.repo.gamer.GamerDetailsRepo;
import com.example.gamingcafe.repo.games.CategoryRepo;
import com.example.gamingcafe.repo.games.GameDetailsRepo;
import com.example.gamingcafe.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class GameController {
    @Autowired
    private GameDetailsRepo gameDetailsRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CredsRepo credsRepo;
    private static final ch.qos.logback.classic.Logger log= (Logger) LoggerFactory.getLogger(GamerController.class);


    @PostMapping("/cat/add")
    public Category addcat(@RequestHeader("Authorization") String token, @RequestBody Category category) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=0) throw new Exception("Not Admin");
            log.info("Added Category Success");
            return categoryRepo.save(category);
        }catch (Exception e){
            log.error("Error");
            throw e;
        }
    }

    @PostMapping("/getall/cat")
    public List<Category> getallcat(@RequestHeader("Authorization") String token) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=0) throw new Exception("Not Admin");
            log.info("Get All Category Success");
            return  categoryRepo.findAll();
        }catch (Exception e){
            log.error("Error");
            throw e;
        }
    }
    @PostMapping("/getall/games")
    public List<ReturnGameObj> getallgames(@RequestHeader("Authorization") String token){
        ArrayList<ReturnGameObj> t=new ArrayList<>();
        List<GameDetails> tt= gameDetailsRepo.findAll();

        for (GameDetails a :
                tt) {
            String cat=categoryRepo.findByCid(a.getCid()).getCtype();
            t.add(new ReturnGameObj(a.getGid(),a.getCost(),cat,a.getGamename()));
        }
        log.info("Get All Games Success");
        return t;
    }

    @PostMapping("/getall/games/cat/{id}")
    public List<ReturnGameObj> getallgamesbyCat(@RequestHeader("Authorization") String token,@PathVariable int id){
        ArrayList<ReturnGameObj> t=new ArrayList<>();
        List<GameDetails> tt= gameDetailsRepo.findAll();
        for (GameDetails a :
                tt) {
            String cat=categoryRepo.findByCid(id).getCtype();
            if(a.getCid()==id)
                t.add(new ReturnGameObj(a.getGid(),a.getCost(),cat,a.getGamename()));
        }
        log.info("Get All Game Category Success");
        return t;
    }

    @PostMapping("/game/add")
    public GameDetails addgame(@RequestHeader("Authorization") String token, @RequestBody GameDetails gameDetails) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=0) throw new Exception("Not Admin");
            if(categoryRepo.findByCid(gameDetails.getCid())==null) throw new Exception("No Category Found");
            log.info("Added Game Success");
            return gameDetailsRepo.save(gameDetails);
        }catch (Exception e){
            log.error("Error");
            throw e;
        }
    }

    @PostMapping("/get/gamesbylist")
    public List<ReturnGameObj> getbyidlist(@RequestHeader("Authorization") String token,@RequestBody List<Integer> listofnum) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            ArrayList<ReturnGameObj> t=new ArrayList<ReturnGameObj>();
            for (int a: listofnum) {
                GameDetails g=gameDetailsRepo.findByGid(a);
                String cate=categoryRepo.findByCid(g.getCid()).getCtype();
                ReturnGameObj r=new ReturnGameObj(g.getGid(),g.getCost(),cate,g.getGamename());
                t.add(r);
            }
            log.info("Fetch Games by Category Success");
            return t;
        }catch (Exception e){
            log.error("Error");
            throw e;
        }
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class ReturnGameObj{
    private int gid,cost;
    private String cate,name;

}