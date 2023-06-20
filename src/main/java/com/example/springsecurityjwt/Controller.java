package com.example.springsecurityjwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.UserService.User;
import com.example.UserService.UserService;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class Controller {
    @RequestMapping({"/Hello"})
    public String hello(){
        return "Hello World yeeeeyyy..Stesh here..Just getting started.Y'all not ready for me";

    }
    @Autowired
    UserService userService;
    @RequestMapping(value = "admin/saveUser", method = RequestMethod.POST)
    public ResponseEntity<User>saveUser(@RequestBody User user){
        userService.saveUser(user);
        return ResponseEntity.created(null).body(user);
    }

    Map<String , String>Response=new HashMap<>();
    JsonMapper mapper = new JsonMapper();
@RequestMapping(value ="/refresh" , method = RequestMethod.POST)
    public void refreshToken(HttpServletRequest request , HttpServletResponse response)throws Exception{
    Algorithm algo = Algorithm.HMAC256("secret".getBytes());
    response.setContentType("application/json");
    String authheader = request.getHeader("Authorization");
    if (authheader == null){
        Response.put("error", "no authorization token");
        mapper.writeValue(response.getOutputStream(), Response);
    }else{
        if (authheader.startsWith("Refresh ")){
            try{
                String Token = authheader.substring("Refresh ".length());
                JWTVerifier verifier= JWT.require(algo).build();
                DecodedJWT decodedJWT = verifier.verify(Token);
                User user = userService.getUser(decodedJWT.getSubject());

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(user.getRole()));

                String accessToken = JWT.create()
                        .withSubject(user.getUserName())
                        .withIssuer("me")
                        .withExpiresAt(new Date(System.currentTimeMillis()+2*60*1000))
                        .withClaim("role",authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .sign(algo);
                Response.put("accessToken" , accessToken );
                Response.put("refreshToken" , Token);
                mapper.writeValue(response.getOutputStream(), Response);
            } catch (Exception e){
                Response.put("error", e.getMessage());
                mapper.writeValue(response.getOutputStream(), Response);
            }
        }
    }
}
}
