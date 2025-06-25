package com.project.MovieReviewer.utility;

import com.project.MovieReviewer.model.Role;
import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.UserProfile;
import com.project.MovieReviewer.repository.RoleRepository;
import com.project.MovieReviewer.repository.UserProfileRepository;
import com.project.MovieReviewer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminCreator implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository userProfileRepository;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name("ROLE_ADMIN")
                                .build()
                ));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name("ROLE_USER")
                                .build()
                ));

        String adminEmail = "admin@example.com";
        String adminPassword = "root";
        String adminUsername = "admin";
        String adminFirstName = "Admin";
        String adminLastName = "Root";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(adminRole, userRole))
                    .build();


            UserProfile profile = UserProfile.builder()
                    .user(admin)
                    .username(adminUsername)
                    .firstName(adminFirstName)
                    .lastName(adminLastName)
                    .build();
            profile.setUser(admin);

            admin.setUserProfile(profile);
            userRepository.save(admin);

            System.out.println("Admin user created: " + adminEmail + " / " + adminPassword);
        } else {
            System.out.println("Admin user already existent (" + adminEmail + ")");
        }
    }
}
