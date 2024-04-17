package com.example.api.controller;

import com.example.api.dto.UserDto;
import com.example.api.dto.response.Response;
import com.example.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Shad Ahmad
 */

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * Handles the incoming POST API "/signup"
     *
     * @param userDto The request body containing user details
     * @return A Response object containing the user details
     */
    @PostMapping("/signup")
    public ResponseEntity<Response<Object>> signup(@RequestBody @Valid UserDto userDto) {
        UserDto registeredUser = userService.registerUser(userDto);
        return ResponseEntity.ok().body(Response.ok().setPayload(registeredUser));
    }

}


