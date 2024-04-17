package com.example.api.mapper;

import com.example.api.dto.RoleDto;
import com.example.api.dto.UserDto;
import com.example.api.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMobileNumber(user.getMobileNumber());
        userDto.setRoles(new HashSet<RoleDto>(user.getRoles().stream()
                .map(role -> new ModelMapper().map(role, RoleDto.class))
                .collect(Collectors.toSet())));
        return userDto;

    }
}
