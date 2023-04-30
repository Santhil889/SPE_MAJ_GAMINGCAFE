package com.example.gamingcafe.controller.bill;

import ch.qos.logback.classic.Logger;
import com.example.gamingcafe.controller.auth.AuthController;
import com.example.gamingcafe.model.auth.Creds;
import com.example.gamingcafe.model.bill.BillUser;
import com.example.gamingcafe.model.bill.GamerBill;
import com.example.gamingcafe.repo.auth.CredsRepo;
import com.example.gamingcafe.repo.bill.BillUserRepo;
import com.example.gamingcafe.repo.bill.GamerBillRepo;
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

@CrossOrigin
@RestController
public class BillController {
    @Autowired
    private GameDetailsRepo gameDetailsRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CredsRepo credsRepo;
    @Autowired
    private BillUserRepo billUserRepo;

    @Autowired
    private GamerBillRepo gamerBillRepo;

    @PostMapping("/add/bill")
    public String savebill(@RequestHeader("Authorization") String token, @RequestBody List<BillUser> ls) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=0) throw new Exception("Not Admin");
            GamerBill gb= new GamerBill(ls.get(0).getGid(),ls.get(0).getBillid());
            gamerBillRepo.save(gb);
            List<BillUser> xyz= new ArrayList<BillUser>();
            for (BillUser b: ls) {
                int costperhour= gameDetailsRepo.findById(b.getGameid()).get().getCost();
                BillUser d= new BillUser(b.getGid(),b.getBillid(),b.getGameid(),b.getHours(),b.getHours() * costperhour,b.getFilledtime());
                xyz.add(d);
            }
            billUserRepo.saveAll(xyz);
            return "Added bill items";
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/get/billid/{id}")
    public Integer getBillId(@RequestHeader("Authorization") String token,@PathVariable int id) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=0) throw new Exception("Not Admin");
            List<GamerBill> g=gamerBillRepo.xyzfalana(id);
            return g.size()+1;
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/get/bills/{gid}")
    public List<GamerBill> getbillbyuser(@RequestHeader("Authorization") String token,@PathVariable int gid) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if((c.getRole()==0) || (c.getId()==gid)) {
                return gamerBillRepo.findAllByGamerid(gid);
            }else {
                throw new Exception("Unauthorized");
            }
        }catch (Exception e){
            throw new Exception("Something went wrong");
        }
    }

    @PostMapping("/get/bill/items")
    public List<CustomObj> getbillitems(@RequestHeader("Authorization") String token,@RequestBody GetBillRequest getBillRequest) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            ArrayList<CustomObj> ret= new ArrayList<CustomObj>();
            if((c.getRole()==0) || (c.getId()==getBillRequest.getGamerid())) {
                List<BillUser> t= billUserRepo.findAllByGidAndBillid(getBillRequest.getGamerid(),getBillRequest.getBillid());
                for(BillUser a:t){

                    CustomObj c1= new CustomObj(gameDetailsRepo.findByGid(a.getGameid()).getGamename(),categoryRepo.findByCid(gameDetailsRepo.findByGid(a.getGameid()).getCid()).getCtype(),a.getHours(),gameDetailsRepo.findByGid(a.getGameid()).getCost(),a.getTotal());
                    ret.add(c1);
                }
                return  ret;
            }
            else {
                throw  new Exception("Unauthorized");
            }
        }catch (Exception e){
            throw e;
        }
    }

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class GetBillRequest {
    private int gamerid;
    private int billid;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class CustomObj {
    private String gamename,catename;
    private int hours,cost,total;
}