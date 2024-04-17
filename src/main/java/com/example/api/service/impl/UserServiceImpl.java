package com.example.api.service.impl;

import com.example.api.dto.RoleDto;
import com.example.api.dto.UserDto;
import com.example.api.dto.request.UserSignupRequest;
import com.example.api.entity.Role;
import com.example.api.entity.User;
import com.example.api.entity.UserRole;
import com.example.api.exception.EntityType;
import com.example.api.exception.ExceptionType;
import com.example.api.mapper.UserMapper;
import com.example.api.repository.RoleRepository;
import com.example.api.repository.UserRepository;
import com.example.api.service.BusReservationService;
import com.example.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.api.exception.EntityType.ROLE;
import static com.example.api.exception.EntityType.USER;
import static com.example.api.exception.ExceptionType.DUPLICATE_ENTITY;
import static com.example.api.exception.ExceptionType.ENTITY_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BusReservationService busReservationService;
    private final ModelMapper modelMapper;


    @Override
    public UserDto registerUser(UserDto userDto) {
        Role userRole = roleRepository.findByName("ROLE_USER"); // Assuming you have a method to find role by name
        User user = userRepository.findByEmail(userDto.getEmail());

        if (user == null) {
            User newUser = new User();
            newUser.setFirstName(userDto.getFirstName());
            newUser.setLastName(userDto.getLastName());
            newUser.setPassword(userDto.getPassword());
            newUser.setEmail(userDto.getEmail());
            newUser.setMobileNumber(userDto.getMobileNumber());
            newUser.setAdmin(userDto.isAdmin());

            // Set default role for the user
            if (userRole != null) {
                newUser.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            }

            userRepository.save(newUser); // Save the new user to the database

            return convertToDto(newUser);
        }

        // Handle duplicate user registration or other scenarios
        throw new RuntimeException("User with email " + userDto.getEmail() + " already exists");
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPassword(user.getPassword()); // Consider not setting password in DTO for security reasons
        userDto.setMobileNumber(user.getMobileNumber());
        userDto.setAdmin(user.isAdmin());
        // Convert roles to RoleDto and set
        userDto.setRoles(user.getRoles().stream().map(role -> {
            RoleDto roleDto = new RoleDto();
            roleDto.setName(role.getName());
            return roleDto;
        }).collect(Collectors.toSet()));

        return userDto;
    }




    @Override
    public UserDto signup(UserDto userDto) {
        Role userRole;
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            if (userDto.isAdmin()) {
                userRole = roleRepository.findByRole(UserRole.ADMIN.name());
            } else {
                userRole = roleRepository.findByRole(UserRole.PASSENGER.name());
            }
            user = new User()
                    .setEmail(userDto.getEmail())
                    .setPassword(passwordEncoder.encode(userDto.getPassword()))
                    .setRoles(new HashSet<>(Arrays.asList(userRole)))
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setMobileNumber(userDto.getMobileNumber());
            return UserMapper.toUserDto(userRepository.save(user));
        }
        throw throwException(USER, DUPLICATE_ENTITY, userDto.getEmail());
    }

    /**
     * Search an existing user
     *
     * @param email
     * @return
     */

    @Override
    public UserDto findUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDto.class);
        }
        throw throwException(USER, ENTITY_NOT_FOUND, email);


    }

    @Override
    public UserDto updateProfile(UserDto userDto) {

        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if (user.isPresent()) {
            User userModel = user.get();
            userModel.setFirstName(userDto.getFirstName());
            userModel.setLastName(userDto.getLastName());
            userModel.setMobileNumber(userDto.getMobileNumber());
            return UserMapper.toUserDto(userRepository.save(userModel));

        }

        throw throwException(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }

    @Override
    public UserDto changePassword(UserDto userDto, String newPassword) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if (user.isPresent()) {
            User userModel = user.get();
            userModel.setPassword(passwordEncoder.encode(newPassword));
            return UserMapper.toUserDto(userRepository.save(userModel));
        }
        throw throwException(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }


    /**
     * Returns a new RuntimeException
     *
     * @param entityType
     * @param exceptionType
     * @param args
     * @return
     */
  /*  private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return throwException(entityType, exceptionType, args);
    }
*/
    private RuntimeException throwException(EntityType entityType, ExceptionType exceptionType, String args) {

        return throwException(entityType, exceptionType, args);
    }
}
