package com.project.MovieReviewer.repository;

import com.project.MovieReviewer.model.UserProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
}
