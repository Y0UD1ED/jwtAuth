package com.example.jwtAuth.mappers;

import com.example.jwtAuth.dtos.LeaderboardDto;
import com.example.jwtAuth.models.UsersCourses;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {

    public UsersCourses passingDtoToPassing(LeaderboardDto leaderboardDto);
}
