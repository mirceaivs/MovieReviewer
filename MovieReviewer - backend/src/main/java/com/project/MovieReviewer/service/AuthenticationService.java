package com.project.MovieReviewer.service;

import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.dto.LoginDTO;
import com.project.MovieReviewer.model.dto.RegisterDTO;

public interface AuthenticationService {
    User register(RegisterDTO newUser);
    User login(LoginDTO user);

}
