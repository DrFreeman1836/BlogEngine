package org.example.main.service.general;

import java.security.Principal;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.request.RqCommentDto;
import org.example.main.dto.response.RsErrors;
import org.example.main.model.PostComments;
import org.example.main.repository.PostCommentRepository;
import org.example.main.repository.PostRepository;
import org.example.main.repository.UserRepository;
import org.example.main.service.auth.AuthService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostRepository postRepository;

  private final PostCommentRepository commentRepository;

  private final AuthService authService;

  private final UserRepository userRepository;


  public RsErrors addCommentToPost(RqCommentDto commentDto, Principal principal) {
    RsErrors errors = null;
    errors = checkComment(errors, commentDto);
    if (errors != null) {
      return errors;
    }

    createComment(commentDto, principal);
    return null;
  }

  private void createComment(RqCommentDto commentDto, Principal principal) {
    PostComments comment = new PostComments();
    comment.setPost(postRepository.getById(commentDto.getPostId()));
    comment.setUser(authService.getCurrentUser(principal));
    comment.setTime(new Date());
    comment.setText(commentDto.getText());
    comment.setParentId(commentDto.getParentId());
    commentRepository.save(comment);
    commentRepository.flush();
  }

  private RsErrors checkComment(RsErrors errors, RqCommentDto commentDto) {
    String text = commentDto.getText();
    if (text == null) {
      errors = errors == null ? new RsErrors() : errors;
      errors.setText("Текс публикации не установлен");
    }
    if (commentDto.getParentId() != null && !userRepository.existsById(commentDto.getParentId())) {
      errors = errors == null ? new RsErrors() : errors;
      errors.setText("Передан несуществвующий id пользователя");
    }
    if (!postRepository.existsById(commentDto.getPostId())) {
      errors = errors == null ? new RsErrors() : errors;
      errors.setText("Передан несуществвующий id поста");
    }
    return errors;
  }

}
