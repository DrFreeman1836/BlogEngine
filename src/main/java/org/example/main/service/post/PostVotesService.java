package org.example.main.service.post;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.main.model.Post;
import org.example.main.model.PostVotes;
import org.example.main.model.User;
import org.example.main.repository.PostRepository;
import org.example.main.repository.PostVotesRepository;
import org.example.main.service.auth.AuthService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostVotesService {

  private final PostVotesRepository postVotesRepository;

  private final PostRepository postRepository;

  private final AuthService authService;

  private final static Byte LIKE = 1;
  private final static Byte DISLIKE = -1;

  public Boolean addLike(Integer postId, Principal principal) {
    Post post = postRepository.getById(postId);
    User user = authService.getCurrentUser(principal);
    Optional<PostVotes> optionalVotes = postVotesRepository.findByUserAndPost(user, post);
    if (checkVotes(optionalVotes, LIKE)) {
      createVotes(LIKE, user, post);
      return true;
    } else {
      return false;
    }
  }

  public Boolean addDislike(Integer postId, Principal principal) {
    Post post = postRepository.getById(postId);
    User user = authService.getCurrentUser(principal);
    Optional<PostVotes> optionalVotes = postVotesRepository.findByUserAndPost(user, post);
    if (checkVotes(optionalVotes, DISLIKE)) {
      createVotes(DISLIKE, user, post);
      return true;
    } else {
      return false;
    }
  }

  private Boolean checkVotes(Optional<PostVotes> postVotes, Byte value) {
    if (postVotes.isPresent()) {
      if (postVotes.get().getValue() == value) {
        return false;
      } else {
        deleteVotes(postVotes.get());
        return true;
      }
    } else {
      return true;
    }
  }

  private void createVotes(Byte value, User user, Post post) {
    PostVotes postVotes = new PostVotes();
    postVotes.setPost(post);
    postVotes.setUser(user);
    postVotes.setValue(value);
    postVotes.setTime(new Date());
    postVotesRepository.save(postVotes);
    postVotesRepository.flush();
  }

  private void deleteVotes(PostVotes postVotes) {
    postVotesRepository.delete(postVotes);
  }

}
/**
 * {"count":5,"posts":[{"id":12,"timestamp":1684263390,"user":{"id":3,"name":"Витек"},"title":"Заголовок","announce":"Пост просто пост и ничего более поста. Самый обыкновенный, ничем не привлекательны пост...","likeCount":1,"disLikeCount":0,"commentCount":0,"viewCount":4},{"id":6,"timestamp":1395086400,"user":{"id":1,"name":"Виктор"},"title":"spring","announce":"spring...","likeCount":0,"disLikeCount":1,"commentCount":2,"viewCount":14},{"id":2,"timestamp":1390230000,"user":{"id":1,"name":"Виктор"},"title":"Уровни изоляции","announce":"read uncommited\r\nread commited\r\nrepitable read\r\nserializable...","likeCount":0,"disLikeCount":1,"commentCount":1,"viewCount":6},{"id":3,"timestamp":1390226400,"user":{"id":1,"name":"Виктор"},"title":"Память и ссылки","announce":"Ссылки бывают сильные мягкие слабые и фантомные...","likeCount":0,"disLikeCount":0,"commentCount":0,"viewCount":0},{"id":1,"timestamp":1369076400,"user":{"id":1,"name":"Виктор"},"title":"Состояния Entity","announce":"Бывают 4 состояния сущности hibernate:\r\n1- trasient\r\n2- persistent\r\n3- datached\r\n4- removed...","likeCount":0,"disLikeCount":1,"commentCount":0,"viewCount":0}]}
 */
