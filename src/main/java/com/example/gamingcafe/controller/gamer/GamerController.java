package com.example.gamingcafe.controller.gamer;

import ch.qos.logback.classic.Logger;
import com.example.gamingcafe.controller.bill.BillController;
import com.example.gamingcafe.model.auth.Creds;
import com.example.gamingcafe.model.gamer.GamerDetails;
import com.example.gamingcafe.repo.auth.CredsRepo;
import com.example.gamingcafe.repo.gamer.GamerDetailsRepo;
import com.example.gamingcafe.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class GamerController {
    @Autowired
    private GamerDetailsRepo gamerDetailsRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CredsRepo credsRepo;

    @PostMapping("/gamer/save")
    public String savegamerdetails(@RequestBody GamerDetails gamerDetails) throws Exception{
        try{
            gamerDetailsRepo.save(gamerDetails);
            return "Gamer Details Added";
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/gamer/get/{id}")
    public GamerDetails getbyid(@RequestHeader("Authorization") String token,@PathVariable int id) throws Exception{
        try {
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=0) throw new Exception("Not Admin");
            return gamerDetailsRepo.findByGid(id);
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/gamer/getactiv/{id}")
    public ReturnObj getactiv(@RequestHeader("Authorization") String token,@PathVariable int id) throws Exception{
        try {
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=0) throw new Exception("Not Admin");
            Creds at=credsRepo.findById(id);
            return new ReturnObj(at.getId(), at.getVerified(), at.getUsername(), at.getEmail());
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/gamer/getall")
    public List<ReturnObj> getallgamer(@RequestHeader("Authorization") String token) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole() != 0) throw new Exception("Not Admin");
            List<Creds> a= new ArrayList<Creds>();
            ArrayList<ReturnObj> temp= new ArrayList<>();
            a=credsRepo.findAll();
            for (Creds at : a) {
                if(at.getRole()!=0) {
                    ReturnObj r = new ReturnObj(at.getId(), at.getVerified(), at.getUsername(), at.getEmail());
                    temp.add(r);
                }
            }
            return temp;
        }catch (Exception e){
            throw e;
        }
    }


    @PostMapping("/gamer/getall/activ")
    public List<ReturnObj> getallgameractiv(@RequestHeader("Authorization") String token) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole() != 0) throw new Exception("Not Admin");
            List<Creds> a= new ArrayList<Creds>();
            ArrayList<ReturnObj> temp= new ArrayList<>();
            a=credsRepo.findAll();
            for (Creds at : a) {
                if(at.getRole()!=0 && at.getVerified()==1) {
                    ReturnObj r = new ReturnObj(at.getId(), at.getVerified(), at.getUsername(), at.getEmail());
                    temp.add(r);
                }
            }
            return temp;
        }catch (Exception e){
            throw e;
        }
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class ReturnObj{
    private  int id,verified;
    private String username,email;

}
