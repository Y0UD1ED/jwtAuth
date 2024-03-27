package com.example.jwtAuth.mappers;

import com.example.jwtAuth.dtos.RegRequest;
import com.example.jwtAuth.dtos.UserDto;
import com.example.jwtAuth.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(source = "username", target = "email")
    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "currentLevel",target = "currentLevel")
    public UserDto userToUserDto(User user);

    @Mapping(source = "email", target = "username")
    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "doW", target = "doW")
    @Mapping(source = "currentLevel",target = "currentLevel")
    public User userDtoToUser(UserDto userDto);

    @Mapping(source = "email", target = "username")
    @Mapping(source = "roles", target = "roles")
    public User RegRequestToUser(RegRequest regRequest);
}
