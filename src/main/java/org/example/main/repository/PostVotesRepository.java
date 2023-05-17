package org.example.main.repository;

import java.util.Optional;
import org.example.main.model.Post;
import org.example.main.model.PostVotes;
import org.example.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {

  Optional<PostVotes> findByUserAndPost(User user, Post post);

}
