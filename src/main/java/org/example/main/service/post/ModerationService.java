package org.example.main.service.post;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.request.RqModerationStatus;
import org.example.main.dto.response.RsPostDto;
import org.example.main.model.ModerationStatus;
import org.example.main.model.Post;
import org.example.main.model.User;
import org.example.main.repository.PostRepository;
import org.example.main.service.auth.AuthService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModerationService {

  private final AuthService authService;

  private final PostRepository postRepository;

  public List<RsPostDto> getPostsToModeration(ModerationStatus status) {
    List<RsPostDto> listPosts = postRepository.findByIsActiveAndModerationStatus(true, status)
        .stream().map(p -> {
          RsPostDto post = new RsPostDto();
          post.fillFields(p);
          return post;
    }).collect(Collectors.toList());
    return listPosts;
  }

  public Boolean setModerationStatus(RqModerationStatus moderationStatus, Principal principal) {
    User currentUser = authService.getCurrentUser(principal);
    if (!currentUser.getIsModerator()) {
      return false;
    }
    try {
      updateModerationStatus(moderationStatus.getPostId(), moderationStatus.getDecision(),
          currentUser.getId());
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private void updateModerationStatus(Integer postId, String status, Integer moderatorId) throws Exception {
    Post post = postRepository.getById(postId);
    post.setModeratorId(moderatorId);
    post.setModerationStatus(status.equals("accept") ? ModerationStatus.ACCEPTED : ModerationStatus.DECLINED);
    postRepository.flush();
  }

}
