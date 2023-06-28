package com.agiboard.controller;

import com.agiboard.dto.AuthTokenDTO;
import com.agiboard.dto.UserDTO;
import com.agiboard.entity.User;
import com.agiboard.service.UserServiceInterface;
import com.agiboard.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "${CORS}", maxAge = 3600)
@RequestMapping("/api/token")
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceInterface userServiceInterface;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager,
                          UserServiceInterface userServiceInterface) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userServiceInterface = userServiceInterface;
    }

    @GetMapping
    public void wakeUpDyno() {
    }

    @PostMapping(value = "/generate-token")
    public ResponseEntity<AuthTokenDTO> generateToken(@RequestBody UserDTO loginUser) throws AuthenticationException {
        logger.info("Attempting to authenticate user " + loginUser.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        final User user = userServiceInterface.findOne(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.status(200).body(new AuthTokenDTO(token, user.getUsername()));
    }
}
