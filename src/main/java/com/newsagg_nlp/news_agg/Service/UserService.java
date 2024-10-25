package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Repo.UserRepo;
import com.newsagg_nlp.news_agg.dto.LoginRequest;
import com.newsagg_nlp.news_agg.dto.LoginResponse;
import com.newsagg_nlp.news_agg.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    //CRUD OPERATIONS
    public UserEntity createUser(UserEntity user) { //creates a new user

        UserEntity newUser = new UserEntity();
        newUser.setEmail(user.getEmail());
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        newUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return userRepo.save(newUser);
    }

    public Optional<UserEntity> getUserById(String userId) {// retrieves a user from database and returns optional object if not present
        return userRepo.findById(userId);
    }

    public List<UserEntity> getAllUsers() {//fetch all users
        return userRepo.findAll();
    }

    public UserEntity updateUser(String userId, UserEntity updatedUser) {//update a user
        Optional<UserEntity> existingUser = userRepo.findById(userId);

        if (existingUser.isPresent()) {

            UserEntity user = existingUser.get();
            user.setFirstname(updatedUser.getFirstname());
            user.setLastname(updatedUser.getLastname());
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            return userRepo.save(user);
        }
        return null;
    }

    public void deleteUser(String userId) {//deletes a user
        userRepo.deleteById(userId);
    }



    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);


        LoginResponse response = new LoginResponse(userDetails.getUsername(), jwtToken);

        return ResponseEntity.ok(response);
    }
}




