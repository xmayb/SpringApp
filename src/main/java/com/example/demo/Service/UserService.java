package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DAO.UserRequestDTO;
import com.example.demo.DAO.UserResponseDTO;
import com.example.demo.Entity.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Random;
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
        userDAO.createUser(user);
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
        userDAO.createUser(user);
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

        userDAO.createUser(user); // void

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

    //Validation helper
    public boolean isValidPassword(String password) {
        //Simple validation
        char[] password = new char[8];
        int min = 48;
        int max = 122;
        //String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])$";
        Random r = new Random();
        for(int i = 0; i < password.length; i++) {
            byte bytes = (byte)(min + r.nextInt(max - min + 1));
            if(password != null && !(bytes > 57 && bytes < 65) || !(bytes > 90 && bytes < 97) ) {
                password[i] = bytes;
                System.out.println("Passwrod is passing ");
            }
        }

    }





}
