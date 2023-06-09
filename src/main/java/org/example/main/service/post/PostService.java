package org.example.main.service.post;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.request.RqPostDto;
import org.example.main.dto.response.RsErrorDto;
import org.example.main.dto.response.RsErrors;
import org.example.main.dto.response.RsLoginDto;
import org.example.main.dto.response.RsPostByIdDto;
import org.example.main.dto.response.RsPostDto;
import org.example.main.model.ModerationStatus;
import org.example.main.model.Post;
import org.example.main.model.Tag;
import org.example.main.model.TagToPost;
import org.example.main.model.User;
import org.example.main.repository.PostRepository;
import org.example.main.repository.TagRepository;
import org.example.main.service.auth.AuthService;
import org.example.main.service.general.TagService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  private final TagRepository tagRepository;

  private static final String EMPTY_STRING = "^\\s*$";

  private final AuthService authService;

  private final TagService tagService;

  private static final String MARKUP_REMOVE = "/<[^>]+>/gi";

  public List<RsPostDto> getPosts(ModeSorting mode) {
    List<RsPostDto> listPosts =
        postRepository.findByIsActiveAndModerationStatusAndTimeBefore(true, ModerationStatus.ACCEPTED, new Date()).stream()
        .map(post -> {
          RsPostDto dto = new RsPostDto();
          dto.fillFields(post);
          return dto;
        })
        .collect(Collectors.toList());

    switch (mode) {
      case recent: return getRecentSorted(listPosts);
      case best: return getBestSorted(listPosts);
      case early: return getEarlySorted(listPosts);
      case popular: return getPopularSorted(listPosts);
      default: return listPosts;
    }

  }

  public List<RsPostDto> searchPosts(String query) {
    if (query == null || Pattern.compile(EMPTY_STRING).matcher(query).matches()) {
      return getPosts(ModeSorting.recent);
    }
    return postRepository.findByIsActiveAndModerationStatusAndTimeBeforeAndTextContaining
            (true, ModerationStatus.ACCEPTED, new Date(), query).stream()
        .map(post -> {
          RsPostDto dto = new RsPostDto();
          dto.fillFields(post);
          return dto;
        })
        .collect(Collectors.toList());
  }

  public List<RsPostDto> getPostsByDate(String dateString) {
    return postRepository.getPostsByDate(dateString).stream()
        .map(post -> {
          RsPostDto dto = new RsPostDto();
          dto.fillFields(post);
          return dto;
        })
        .collect(Collectors.toList());
  }

  public List<RsPostDto> getPostsByTag(String tagName) {
    Tag tag = tagRepository.findFirstByName(tagName).orElse(null);
    if (tag == null)
      return new ArrayList<>();
    return tag.getTagToPostList().stream().map(TagToPost::getPost)
        .map(post -> {
          RsPostDto dto = new RsPostDto();
          dto.fillFields(post);
          return dto;
        })
        .collect(Collectors.toList());
  }

  public RsPostByIdDto getPostById(Integer id) {
    Optional<Post> optionalPost = postRepository.findActivePostById(id);
    if (optionalPost.isPresent()) {
      RsPostByIdDto postById = new RsPostByIdDto();
      postById.fillFields(optionalPost.get());
      viewIncrement(optionalPost.get());
      return postById;
    }
    return null;
  }

  public List<RsPostDto> getMyPost(StatusMode status, Principal principal) {
    RsLoginDto loginUser = authService.checkLogin(principal);
    List<Post> listPosts = postRepository.findByUserId(loginUser.getId());

    switch (status) {
      case inactive: return getInactivePosts(listPosts);
      case pending: return getPendingPosts(listPosts);
      case declined: return getDeclinedPosts(listPosts);
      case published: return gePublishedPosts(listPosts);
      default: return listPosts.stream()
          .map(post -> {
            RsPostDto dto = new RsPostDto();
            dto.fillFields(post);
            return dto;
          }).collect(Collectors.toList());
    }
  }

  public RsErrors addPost(RqPostDto postDto, Principal principal) {
    Date date = postDto.getTimestamp();
    RsErrors errors = null;
    errors = checkPost(errors, postDto);
    if (errors != null) {
      return errors;
    }
    if (date.before(new Date())) {
      date = new Date();
    }

    User user = authService.getCurrentUser(principal);
    createPost(postDto, date, user);
    return null;
  }

  public RsErrors updatePost(Integer idPost, RqPostDto postDto, Principal principal) {
    Date date = postDto.getTimestamp();
    RsErrors errors = null;
    errors = checkPost(errors, postDto);
    if (errors != null) {
      return errors;
    }
    if (date.before(new Date())) {
      date = new Date();
    }

    User user = authService.getCurrentUser(principal);
    Post post = postRepository.getById(idPost);
    post.setText(post.getText());
    post.setTitle(post.getTitle());
    post.setIsActive(postDto.getActive() == 1);
    post.setTime(date);
    post.setModerationStatus(!user.getIsModerator() ? ModerationStatus.NEW : post.getModerationStatus());
    tagService.updateTagToPost(post, postDto.getTags());
    postRepository.save(post);
    return null;
  }

  private RsErrors checkPost(RsErrors errors, RqPostDto postDto) {
    String title = postDto.getTitle();
    String text = postDto.getText();
    if (title == null || title.length() < 3) {
      errors = new RsErrors();
      errors.setTitle("Заголовок не установлен или слишком короткий");
    }
    if (text == null || text.length() < 50) {
      errors = errors == null ? new RsErrors() : errors;
      errors.setText("Текс публикации слишком короткий или не установлен");
    }
    return errors;
  }

  private void createPost(RqPostDto postDto, Date time, User user) {
    Post newPost = new Post();
    newPost.setText(postDto.getText());
    newPost.setTitle(postDto.getTitle());
    newPost.setIsActive(postDto.getActive() == 1);
    newPost.setModerationStatus(ModerationStatus.NEW);
    newPost.setUser(user);
    newPost.setTime(time);
    newPost.setViewCount(0);
    Post post = postRepository.save(newPost);
    createTag(post, postDto.getTags());
  }

  private void createTag(Post post, List<String> tags) {
    tagService.addTag(post, tags);
  }

  private void viewIncrement(Post post) {
    post.setViewCount(post.getViewCount() + 1);
    postRepository.save(post);
    postRepository.flush();
  }

  private List<RsPostDto> getInactivePosts(List<Post> defaultList) {
    return defaultList.stream()
        .filter(p -> !p.getIsActive())
        .map(post -> {
          RsPostDto dto = new RsPostDto();
          dto.fillFields(post);
          return dto;
        })
        .collect(Collectors.toList());
  }

  private List<RsPostDto> getPendingPosts(List<Post> defaultList) {
    return defaultList.stream()
        .filter(p -> p.getIsActive() && p.getModerationStatus().equals(ModerationStatus.NEW))
        .map(post -> {
          RsPostDto dto = new RsPostDto();
          dto.fillFields(post);
          return dto;
        })
        .collect(Collectors.toList());
  }

  private List<RsPostDto> getDeclinedPosts(List<Post> defaultList) {
    return defaultList.stream()
        .filter(p -> p.getIsActive() && p.getModerationStatus().equals(ModerationStatus.DECLINED))
        .map(post -> {
          RsPostDto dto = new RsPostDto();
          dto.fillFields(post);
          return dto;
        })
        .collect(Collectors.toList());
  }

  private List<RsPostDto> gePublishedPosts(List<Post> defaultList) {
    return defaultList.stream()
        .filter(p -> p.getIsActive() && p.getModerationStatus().equals(ModerationStatus.ACCEPTED))
        .map(post -> {
          RsPostDto dto = new RsPostDto();
          dto.fillFields(post);
          return dto;
        })
        .collect(Collectors.toList());
  }

  private List<RsPostDto> getRecentSorted(List<RsPostDto> defaultList) {
    return defaultList.stream().sorted(Comparator.comparing(RsPostDto::getTimestamp).reversed()).collect(Collectors.toList());
  }

  private List<RsPostDto> getPopularSorted(List<RsPostDto> defaultList) {
    return defaultList.stream().sorted(Comparator.comparing(RsPostDto::getCommentCount).reversed()).collect(Collectors.toList());
  }

  private List<RsPostDto> getBestSorted(List<RsPostDto> defaultList) {
    return defaultList.stream().sorted(Comparator.comparing(RsPostDto::getLikeCount).reversed()).collect(Collectors.toList());
  }

  private List<RsPostDto> getEarlySorted(List<RsPostDto> defaultList) {
    return defaultList.stream().sorted(Comparator.comparing(RsPostDto::getTimestamp)).collect(Collectors.toList());
  }

}
