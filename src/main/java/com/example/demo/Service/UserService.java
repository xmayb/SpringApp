package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DAO.UserRequestDTO;
import com.example.demo.DAO.UserResponseDTO;
import com.example.demo.Entity.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.regex.Pattern;



@Service
public class UserService {
    private UserDAO userDAO;


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
            throw new IllegalArgumentException("Invalid email");

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

    //Registration logic
    public UserResponseDTO registerUser(User user) {
        // TODO: Implement registration logic including password hashing
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String rawPassword = user.getPassword();
        String hashed = passwordEncoder.encode(rawPassword);
        user.setPassword(hashed);



        //Save user
        userDAO.createUser(user);// save user to the database
        //Return to DTO
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
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
    public void loginUser(User user, String email, String password) {

        if(user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        PasswordEncoder passwordEncoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();//create a password encoder in order to receive an object which is able to encode and decode password
        validatePassword(password);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

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
            throw new IllegalArgumentException("Invalid email");
        }
    }







}
