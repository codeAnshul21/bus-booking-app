package com.example.api.service;

import com.example.api.dto.UserDto;
import com.example.api.dto.request.UserSignupRequest;
import jakarta.validation.Valid;

/**
 * @author Shad Ahmad
 */

public interface UserService {



    UserDto registerUser(UserDto userDto);

    /**
     * Register a new user
     *
     * @param userDto
     * @return
     */

    UserDto signup(UserDto userDto);

    /**
     * Search an existing user
     *
     * @param email
     * @return
     */


    UserDto findUserByEmail(String email);

    /**
     * Update profile of the user
     *
     * @param userDto
     * @return
     */


    UserDto updateProfile(UserDto userDto);


    /**
     * Update password
     *
     * @param newPassword
     * @return
     */
    UserDto changePassword(UserDto userDto, String newPassword);


}
