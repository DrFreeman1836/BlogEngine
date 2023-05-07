package org.example.main.repository;

import java.util.Date;
import java.util.List;
import org.example.main.model.ModerationStatus;
import org.example.main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

  List<Post> findByIsActiveAndModerationStatusAndTimeBefore(Boolean isActive, ModerationStatus moderationStatus, Date time);

}
