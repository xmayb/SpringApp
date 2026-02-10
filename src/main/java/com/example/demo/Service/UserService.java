package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DAO.UserRequestDTO;
import com.example.demo.DAO.UserResponseDTO;
import com.example.demo.Entity.User;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;



@Service
public class UserService {
    private UserDAO userDAO;


    /// Map to store login attempts per email

    private Map<String, LoginAttempt> attemptsMap = new ConcurrentHashMap<>();


    private static final int MAX_ATTEMPTS = 3;
    private static final long WINDOW_MS = 10 * 60 * 1000;
    private static final long BLOCK_MS = 15 * 60 * 1000; // 5 minutes

    private static class LoginAttemp {
        int attempts = 0;
        long lastAttemptTime = 0;
        long blockedUntil = 0;
    }

    private static class LoginAttempt {
        int attempts;
        long lastAttemptTime;
        long blockedUntil;
    }
    ///


    public UserService(UserDAO userDAO) {
        if(userDAO == null) {
            throw new IllegalArgumentException("USerDAO is null");
        }
        this.userDAO = userDAO;
    }

    private final Pattern EMAIL_PATERN =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public  boolean isValidEmail(String email) {
        return email != null && EMAIL_PATERN.matcher(email).matches();
    }

    public void createUser(User user) {
        if(!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid username or password");

        }
        userDAO.createUser(user);// save user to the database
    }

    public User getUserById(Long id) {
        return userDAO.getUserById(id);
    }

    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    public void deleteUser(Long id) {
        userDAO.deleteUser(id);
    }



    //UserRequestDTO
    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = new User(
                dto.getName(),
                dto.getEmail(),
                dto.getPassword()
        );

        userDAO.createUser(user); // save user to the database

        UserResponseDTO response = new UserResponseDTO();
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }

    //UserResponseDTO
    public UserResponseDTO getUserResponseDTO(User user) {//DTO gives us back outwardly
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }

    //UserService hashing pass
    public String hashPassword(String password) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public boolean validatePassword(String password) {
        // Simple validation
        int maxlength = 72;
        if(password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password is too short");
        }
        if(password.length() > maxlength) {
            throw new IllegalArgumentException("Password is too long");
        }

        if(!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new IllegalArgumentException("Password must contain uppercase and special character");
        }


        return true; // if it comes here, the password is valid
    }

    //Validation helper

    //login
    public void loginUser(User user, String email, String password) throws AuthException {

        if(user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        PasswordEncoder passwordEncoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();//create a password encoder in order to receive an object which is able to encode and decode password
        validatePassword(password);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException("Invalid username or password");
        }

        /// Counts of attempts
        LoginAttempt attempt = attemptsMap.getOrDefault(email, new LoginAttempt());
        // Check if user is currently blocked
        long now = System.currentTimeMillis();
        if(attempt.blockedUntil > now) {
            throw new AuthException("Invalid username or password");
        }

        //check if the password does not match
        if(!passwordEncoder.matches(password, user.getPassword())) {
            attempt.attempts++;
            attempt.lastAttemptTime = now;

            if(attempt.attempts >= MAX_ATTEMPTS) {
                attempt.blockedUntil = now + BLOCK_MS;
            }

            attemptsMap.put(email, attempt);
            throw new AuthException("Invalid username or password");
        }

        // Reset attempts on successful login
        attemptsMap.remove(email);


    }

    public void userNameValidation(String name) {
        if(name == null || name.length() < 4 || name.length() > 30) {
            throw new IllegalArgumentException("User is required and username length must be 4-30 characters");
        }
        if(!name.matches("^[a-zA-Z0-9._-]+$")){
            throw new IllegalArgumentException("Username contains invalid characters");
        }
    }

    public void userEmailValidation(String email) {
        if(email == null || email.length() > 254) {
            throw new IllegalArgumentException("Email is required and must be less than 255 characters");
        }
        if (!email.matches("^[\\w.-]+@([\\w-]+\\.)+[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }


}


