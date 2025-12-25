package com.example.demo.Controller;


import com.example.demo.DAO.UserRequestDTO;
import com.example.demo.DAO.UserResponseDTO;
import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;//type:UserService; name of field:userService

    public UserController(UserService userService) {//constructor injection says when you will create Controller gives him already UserService
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {//@RequestBody says take JSON from the request body and convert it to a User object
        userService.createUser(user);//reference from createUser method in UserService
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    //Controller for UserRequest and UserResponseDTO
    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDTO dto) {
        userService.createUser(dto);
        return ResponseEntity.ok().build();
    }




}
