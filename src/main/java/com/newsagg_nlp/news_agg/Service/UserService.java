package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Repo.UserRepo;
import com.newsagg_nlp.news_agg.dto.LoginRequest;
import com.newsagg_nlp.news_agg.dto.LoginResponse;
import com.newsagg_nlp.news_agg.dto.SignupRequest;
import com.newsagg_nlp.news_agg.dto.UserInfoUpdate;
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

import static com.newsagg_nlp.news_agg.utils.Crypt.decrypt;

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
    public ResponseEntity<?> createUser(SignupRequest signupRequest) throws Exception { //creates a new user

        String encryptedPassword = signupRequest.getPassword();
        String secretKey = "123";

        String decryptedPassword = decrypt(encryptedPassword, secretKey);

        System.out.println("Decrypted Password: " + decryptedPassword);

        UserEntity newUser = new UserEntity();
        newUser.setEmail(signupRequest.getEmail());
        newUser.setFirstname(signupRequest.getFirstname());
        newUser.setLastname(signupRequest.getLastname());
        newUser.setUsername(signupRequest.getUsername());
//        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPassword(passwordEncoder.encode(decryptedPassword));
        newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        newUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        UserEntity savedUser = userRepo.save(newUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
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

    public void updateUserInfo(String userId, UserInfoUpdate userInfoUpdate) {
        Optional<UserEntity> userOptional = userRepo.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserEntity user = userOptional.get();

        if (userInfoUpdate.getFirstname() != null) {
            user.setFirstname(userInfoUpdate.getFirstname());
        }
        if (userInfoUpdate.getLastname() != null) {
            user.setLastname(userInfoUpdate.getLastname());
        }
        userRepo.save(user);

    }


    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {
        Authentication authentication;

        String encryptedPassword = loginRequest.getPassword();
        String secretKey = "123";

        System.out.println(encryptedPassword);
        String decryptedPassword = decrypt(encryptedPassword, secretKey);

        System.out.println("Decrypted Password: " + decryptedPassword);
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), decryptedPassword));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        String username=userDetails.getUsername();
        UserEntity user = userRepo.findByUsername(userDetails.getUsername());

        String userId=user.getUserId();


        LoginResponse response = new LoginResponse(username, jwtToken,userId);

        return ResponseEntity.ok(response);
    }
}




