package com.example.UserService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

//    access modifier ,return type, method name (parameterize or not)
//    we only use return when the return type is not void
    public User getUser(String userName){

       return userRepo.findByUserName(userName);
    }
    Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    PasswordEncoder passwordEncoder;

    public void saveUser(User user){
        log.info("saving user");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
            org.springframework.security.core.userdetails.User user2 = new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
            return user2;
    }
}