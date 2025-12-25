package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DAO.UserRequestDTO;
import com.example.demo.DAO.UserResponseDTO;
import com.example.demo.Entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import java.util.UUID;
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




}
