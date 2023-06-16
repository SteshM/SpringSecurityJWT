package com.example.springsecurityjwt;

import com.example.UserService.User;
import com.example.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {
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

}
