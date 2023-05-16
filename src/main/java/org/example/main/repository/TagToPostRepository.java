package org.example.main.repository;

import java.util.List;
import org.example.main.model.Post;
import org.example.main.model.TagToPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagToPostRepository extends JpaRepository<TagToPost, Integer> {

  List<TagToPost> findByPost(Post post);

}
