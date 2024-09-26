package ru.bicev.movie_ratings.utils;

import java.util.List;
import java.util.stream.Collectors;

import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.entitites.Review;
import ru.bicev.movie_ratings.entitites.User;

public class UserConverter {

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUserName(user.getUserName());
        dto.setRole(user.getRole().name());

        List<Long> reviewIds = (user.getReviews() != null) ? user.getReviews().stream()
                .map(Review::getId)
                .collect(Collectors.toList()) : List.of();
        dto.setReviewIds(reviewIds);

        return dto;
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        user.setRole(Role.fromString(userDto.getRole()));

        return user;
    }
}
