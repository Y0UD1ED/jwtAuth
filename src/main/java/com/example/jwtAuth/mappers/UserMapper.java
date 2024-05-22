package com.example.jwtAuth.mappers;

import com.example.jwtAuth.dtos.RegRequest;
import com.example.jwtAuth.dtos.UserInfoDto;
import com.example.jwtAuth.dtos.UserUpdateDto;
import com.example.jwtAuth.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(source = "login", target = "email")
    @Mapping(source = "roles", target = "roles")
    public UserInfoDto userToUserInfoDto(User user);

    @Mapping(source = "email", target = "login")
    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "doW", target = "doW")
    public User userInfoDtoToUser(UserInfoDto userInfoDto);

    @Mapping(source = "email", target = "login")
    public User RegRequestToUser(RegRequest regRequest);

    @Mapping(source = "email", target = "login")
    @Mapping(target = "photo", ignore = true)
    public User UserUpdateDtoToUser(UserUpdateDto userUpdateDto);
}
