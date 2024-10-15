package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;

    }
    //CRUD OPERATIONS
    public UserEntity createUser(UserEntity user){ //creates a new user
        return userRepo.save(user);
    }
    public Optional<UserEntity>getUserById(Integer userId){// retrieves a user from database and returns optional object if not present
        return userRepo.findById(userId);
    }
    public List<UserEntity> getAllUsers(){//fetch all users
        return userRepo.findAll();
    }
    public UserEntity updateUser(Integer userId, UserEntity updatedUser) {//update a user
        Optional<UserEntity> existingUser = userRepo.findById(userId);

        if (existingUser.isPresent()) {

            UserEntity user = existingUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            return userRepo.save(user);
        }
        return null;
    }
    public void deleteUser(Integer userId){//deletes a user
        userRepo.deleteById(userId);
    }


}
