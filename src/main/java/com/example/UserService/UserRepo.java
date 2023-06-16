package com.example.UserService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepo extends JpaRepository<User, Long> {
//    custom query
//    can be native or predefined
    public User findByUserName(String userName);
}
