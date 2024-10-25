package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity authUser=userRepo.findByUsername((username));
        if (authUser != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(authUser.getUsername())
                    .password(authUser.getPassword())
                    .build();
        }

        throw new UsernameNotFoundException("User Not Found with this Usernmae:"+ username);
    }
}
