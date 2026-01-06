package com.example.Backend.Entities.Reviews;


import com.example.Backend.Entities.Users.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewModel, Long> {
    List<ReviewModel> findByContentIdAndContentType(Long contentId, ContentType contentType);
    long countByUser(UserModel user);
}

