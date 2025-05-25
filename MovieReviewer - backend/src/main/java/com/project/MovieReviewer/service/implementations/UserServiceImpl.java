package com.project.MovieReviewer.service.implementations;

import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.UserProfile;
import com.project.MovieReviewer.model.dto.UserFullProfileDTO;
import com.project.MovieReviewer.repository.UserRepository;
import com.project.MovieReviewer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserFullProfileDTO getCurrentUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
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

    @Override
    @Transactional
    public void updateUser(String email, UserFullProfileDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean modified = false;

        if (dto.getEmail() != null && !dto.getEmail().isBlank() && !user.getEmail().equals(dto.getEmail())) {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Emailul este deja folosit!");
            }
            user.setEmail(dto.getEmail());
            modified = true;
        }

        UserProfile profile = user.getUserProfile();
        if (profile != null) {
            if (dto.getUsername() != null && !dto.getUsername().isBlank() && !dto.getUsername().equals(profile.getUsername())) {
                profile.setUsername(dto.getUsername());
                modified = true;
            }
            if (dto.getFirstName() != null && !dto.getFirstName().isBlank() && !dto.getFirstName().equals(profile.getFirstName())) {
                profile.setFirstName(dto.getFirstName());
                modified = true;
            }
            if (dto.getLastName() != null && !dto.getLastName().isBlank() && !dto.getLastName().equals(profile.getLastName())) {
                profile.setLastName(dto.getLastName());
                modified = true;
            }
            if (dto.getBio() != null && !dto.getBio().equals(profile.getBio())) {
                profile.setBio(dto.getBio());
                modified = true;
            }
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (dto.getOldPassword() != null && !passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Parola veche este incorecta!");
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            modified = true;
        }

        if (modified) {
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
