package com.project.MovieReviewer.service.implementations;

import com.project.MovieReviewer.model.Role;
import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.UserProfile;
import com.project.MovieReviewer.model.dto.LoginDTO;
import com.project.MovieReviewer.model.dto.RegisterDTO;
import com.project.MovieReviewer.repository.RoleRepository;
import com.project.MovieReviewer.repository.UserProfileRepository;
import com.project.MovieReviewer.repository.UserRepository;
import com.project.MovieReviewer.service.AuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager, UserProfileRepository userProfileRepository,
                                     RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userProfileRepository = userProfileRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public User register(RegisterDTO newUser) {

        if(userRepository.findByEmail(newUser.getEmail()).isPresent())
            throw new BadCredentialsException("The email address is already in use!");

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found!"));


        UserProfile userProfile = UserProfile.builder()
                .username(newUser.getUsername())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .build();

        User user = User.builder()
                .email(newUser.getEmail())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .userProfile(userProfile)
                .roles(new HashSet<>(Set.of(userRole)))
                .build();

        userProfile.setUser(user);

        return userRepository.save(user);

    }

    @Override
    public User login(LoginDTO user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        return userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials, try again!"));
    }
}
