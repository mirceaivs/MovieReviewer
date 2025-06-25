package com.project.MovieReviewer.service;

import com.project.MovieReviewer.model.dto.UserFullProfileDTO;

public interface UserService {
    UserFullProfileDTO getCurrentUserProfile(String email);
    void updateUser(String email, UserFullProfileDTO req);
    void deleteUser(String email);
}
