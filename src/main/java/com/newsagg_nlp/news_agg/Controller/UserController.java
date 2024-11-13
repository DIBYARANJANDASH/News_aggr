package com.newsagg_nlp.news_agg.Controller;


import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Service.UserService;
import com.newsagg_nlp.news_agg.dto.LoginRequest;
import com.newsagg_nlp.news_agg.dto.PasswordChangeRequest;
import com.newsagg_nlp.news_agg.dto.SignupRequest;
import com.newsagg_nlp.news_agg.dto.UserInfoUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
//@CrossOrigin(origins = "http://127.0.0.1:5500/loginpage/login.html?")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody SignupRequest signupRequest) throws Exception {
        return userService.createUser(signupRequest);
    }

    @PostMapping("/loginUser")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {
        return userService.authenticateUser(loginRequest);
    }

    @GetMapping("/{id}")
    public Optional<UserEntity> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserEntity updateUser(@PathVariable String id, @RequestBody UserEntity updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @PatchMapping("/update-info/{id}")
    public ResponseEntity<?> updateUserInfo(@PathVariable String id, @RequestBody UserInfoUpdate userInfoUpdate){
        try {
            System.out.println(id);
            userService.updateUserInfo(id, userInfoUpdate.getFirstname(), userInfoUpdate.getLastname());
            return ResponseEntity.ok("User info updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user info.");
        }
    }


    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable String id, @RequestBody PasswordChangeRequest request) {
        boolean isUpdated = userService.updateUserPassword(id, request.getCurrentPassword(), request.getNewPassword());
        if (isUpdated) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update password. Incorrect current password.");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}

