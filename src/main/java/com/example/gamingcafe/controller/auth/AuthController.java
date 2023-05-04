package com.example.gamingcafe.controller.auth;

import ch.qos.logback.classic.Logger;
import com.example.gamingcafe.model.auth.AuthRequest;
import com.example.gamingcafe.model.auth.Creds;
import com.example.gamingcafe.repo.auth.CredsRepo;
import com.example.gamingcafe.util.JwtUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CredsRepo credsRepo;
    @Autowired
    private AuthenticationManager authenticationManager;

    private static final ch.qos.logback.classic.Logger log= (Logger) LoggerFactory.getLogger(AuthController.class);

//    @GetMapping("/try/something")
//    public ResponseEntity<String> welcome(@RequestHeader("Authorization") String token) throws Exception{
//        try{
//        token= token.substring(7);
//        String uname=jwtUtil.extractUsername(token);
//        return ResponseEntity.ok("hello "+uname);
//        }catch (Exception e){
//            throw e;
//        }
//
//    }

    @PostMapping("/approve/gamer/{gid}")
    public String approvegamer(@RequestHeader("Authorization") String token,@PathVariable int gid) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole() != 0 ) throw new Exception("Not Admin");
            Creds d=credsRepo.findById(gid);
            d.setVerified(1);
            credsRepo.save(d);
            log.info("Gamer Approved");
            return "Approved user "+d.getUsername();
//            return "Verified user "+d.getUsername();
        }catch (Exception e){
            log.error("Error Occurred");
            throw e;
        }
    }

    @PostMapping("/get/gameridfromtoken")
    public Integer getgamerid(@RequestHeader("Authorization") String token) throws Exception{
        token= token.substring(7);
        String uname=jwtUtil.extractUsername(token);
        Creds c=credsRepo.findByUsername(uname);
        if(c.getRole() != 1 ) throw new Exception("Not GAMER");
        log.info("Fetched Gamer id from token");
        return c.getId();
    }
    @PostMapping("/bann/gamer/{gid}")
    public String banngamer(@RequestHeader("Authorization") String token,@PathVariable int gid) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole() != 0 ) throw new Exception("Not Admin");
            Creds d=credsRepo.findById(gid);
            d.setVerified(0);
            credsRepo.save(d);
            log.info("Gamer Banned");
            return "Banned user "+d.getUsername();
        }catch (Exception e){
            log.error("Error Occurred");
            throw e;
        }
    }

    @PostMapping("/gamer/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception{
        if(authRequest.getRole()!=credsRepo.findByUsername(authRequest.getUsername()).getRole()) throw new Exception("Not Doctor");
        Creds a= credsRepo.findByUsername(authRequest.getUsername());
        if(a.getVerified()==0) throw  new Exception("User not activated");
        try{
            Creds d=credsRepo.findByUsername(authRequest.getUsername());
            if(d.getVerified() == 0) throw new Exception("Not verified");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
            );
        }catch (Exception ex){
            log.error("Error Occurred");
            throw new Exception("Invalid Username/Password");
        }
        log.info("Gamer Logged in");
        return jwtUtil.generateToken(authRequest.getUsername());
    }

    @PostMapping("/gamer/register")
    public Integer newReg(@RequestBody Creds registerRequest) throws Exception{
        try{
            Creds ct = credsRepo.save(registerRequest);
            log.info("Gamer Registered");
            return ct.getId();
        }catch (Exception ex){
//            System.out.println(ex);
            log.error("Login Error");
            throw new Exception(ex);
        }

    }

    @PostMapping("/admin/authenticate")
    public ResponseEntity<String> generateTokenforAdmin(@RequestBody AuthRequest authRequest) throws Exception{
        if(authRequest.getRole() != credsRepo.findByUsername(authRequest.getUsername()).getRole() ) {
            log.error("Unauthorized Login");
            return new ResponseEntity<>(
                    "Bad Credentials",
                    HttpStatus.BAD_REQUEST);
        }
        System.out.println(authRequest.getUsername());
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
            );
        }catch (Exception ex){
            log.error("Invalid Creds Entered");
            throw new Exception("Invalid Username/Password");
        }
        String xyz=jwtUtil.generateToken(authRequest.getUsername());
        System.out.println(xyz + "  is tje generated token");
        log.info("Admin Logged In");
        return new ResponseEntity<>(
                xyz,
                HttpStatus.OK);
    }

    @PostMapping("/get/user")
    public int getid(@RequestHeader("Authorization") String token) throws  Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=1) throw new Exception("Not Gamer");
            log.info("Get User Details Success");
            return  c.getId();
        }catch (Exception e){
            log.error("Error Occurred");
            throw e;
        }
    }
}
