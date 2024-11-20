package com.newsagg_nlp.news_agg.Controller;


import com.newsagg_nlp.news_agg.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    // Temporary in-memory token blacklist (use a database or caching system in production)
    private static final Set<String> tokenBlacklist = new HashSet<>();

    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // Extract the token
                if (jwtUtils.validateJwtToken(token)) {
                    tokenBlacklist.add(token); // Add the token to the blacklist
                    logger.info("Token added to blacklist: {}", token);
                    return ResponseEntity.ok("Logged out successfully.");
                }
            }
            return ResponseEntity.badRequest().body("Invalid or missing token.");
        } catch (Exception e) {
            logger.error("Logout error: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Logout failed.");
        }
    }

    // Utility method to check if a token is blacklisted
    public static boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}

