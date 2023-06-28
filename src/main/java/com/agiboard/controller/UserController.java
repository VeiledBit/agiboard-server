package com.agiboard.controller;

import com.agiboard.dto.UserDTO;
import com.agiboard.entity.User;
import com.agiboard.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "${CORS}", maxAge = 3600)
@RequestMapping(value = "/api/users/")
public class UserController {

    final UserServiceInterface userServiceInterface;
    private final BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    public UserController(UserServiceInterface userServiceInterface, BCryptPasswordEncoder bcryptEncoder) {
        this.userServiceInterface = userServiceInterface;
        this.bcryptEncoder = bcryptEncoder;
    }

    @PostMapping(value = "saveUser", consumes = "application/json")
    public ResponseEntity<Void> saveUser(@RequestBody UserDTO payload) {
        String name = payload.getName();
        String username = payload.getUsername();
        String password = payload.getPassword();

        User existingUser = userServiceInterface.findOne(username);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        User user = new User();
        user.setName(name.trim());
        user.setUsername(username);
        user.setPassword(bcryptEncoder.encode(password));
        user.setBoards(new ArrayList<>());

        userServiceInterface.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping(value = "deleteUser", consumes = "application/json")
    public ResponseEntity<Void> deleteUser(@RequestBody UserDTO payload) {
        String username = payload.getUsername();
        User user = userServiceInterface.findOne(username);

        userServiceInterface.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
