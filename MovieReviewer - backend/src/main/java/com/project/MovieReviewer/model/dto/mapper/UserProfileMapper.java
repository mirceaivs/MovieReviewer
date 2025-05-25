package com.project.MovieReviewer.model.dto.mapper;

import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.UserProfile;
import com.project.MovieReviewer.model.dto.UserFullProfileDTO;

//public class UserProfileMapper {
//
//    public static UserFullProfileDTO toDTO(User user) {
//        UserProfile profile = user.getUserProfile();
//        return UserFullProfileDTO.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .username(profile != null ? profile.getUsername() : null)
//                .firstName(profile != null ? profile.getFirstName() : null)
//                .lastName(profile != null ? profile.getLastName() : null)
//                .bio(profile != null ? profile.getBio() : null)
//                .createdAt(profile != null ? profile.getCreatedAt() : null)
//                .password(user.getPassword())
//                .build();
//    }
//}
public class UserProfileMapper {
    public static UserFullProfileDTO toDTO(User user) {
        UserProfile profile = user.getUserProfile();
        return UserFullProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(profile != null ? profile.getUsername() : null)
                .firstName(profile != null ? profile.getFirstName() : null)
                .lastName(profile != null ? profile.getLastName() : null)
                .bio(profile != null ? profile.getBio() : null)
                .createdAt(profile != null ? profile.getCreatedAt() : null)
                .build();
    }
}
