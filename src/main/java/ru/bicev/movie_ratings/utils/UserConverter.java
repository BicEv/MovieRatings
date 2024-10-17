package ru.bicev.movie_ratings.utils;

import java.util.List;
import java.util.stream.Collectors;

import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.entitites.Review;
import ru.bicev.movie_ratings.entitites.User;

/**
 * Utility class for converting between User entities and UserDto objects.
 * <p>
 * This class provides methods to convert a User entity to a UserDto and
 * vice versa, facilitating the transfer of user data between different layers
 * of the application.
 * </p>
 */
public class UserConverter {

    /**
     * Converts a User entity to a UserDto
     * 
     * @param user the user to be converted
     * @return a UserDto based on the provided User or {@code null} if the input is
     *         null
     */
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

    /**
     * Converts a UserDto to a User entity
     * 
     * @param userDto the UserDto to be converted
     * 
     * @return a User entity based on provided UserDto
     */
    public static User toEntity(UserDto userDto) {
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        user.setRole(Role.fromString(userDto.getRole()));

        return user;
    }
}
