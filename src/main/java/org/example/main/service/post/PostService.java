package org.example.main.service.post;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.response.RsPostDto;
import org.example.main.dto.response.RsUserDto;
import org.example.main.model.ModerationStatus;
import org.example.main.model.Post;
import org.example.main.model.Tag;
import org.example.main.model.TagToPost;
import org.example.main.repository.PostRepository;
import org.example.main.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  private final TagRepository tagRepository;

  private static final String EMPTY_STRING = "^\\s*$";

  private static final String MARKUP_REMOVE = "/<[^>]+>/gi";

  public List<RsPostDto> getPosts(ModeSorting mode) {
    List<RsPostDto> listPosts =
        postRepository.findByIsActiveAndModerationStatusAndTimeBefore(true, ModerationStatus.ACCEPTED, new Date()).stream()
        .map(post -> RsPostDto.builder()
            .id(post.getId())
            .timestamp(post.getTime().getTime()/1000)
            .user(RsUserDto.builder().id(post.getUser().getId()).name(post.getUser().getName()).build())
            .title(post.getTitle())
            .announce(getAnnounce(post.getText()))
            .likeCount((int) post.getPostVotesList().stream().filter(postVotes -> postVotes.getValue() == 1).count())
            .disLikeCount((int) post.getPostVotesList().stream().filter(postVotes -> postVotes.getValue() == -1).count())
            .commentCount(post.getPostCommentsList().size())
            .viewCount(post.getViewCount())
            .build())
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
        .map(post -> RsPostDto.builder()
            .id(post.getId())
            .timestamp(post.getTime().getTime()/1000)
            .user(RsUserDto.builder().id(post.getUser().getId()).name(post.getUser().getName()).build())
            .title(post.getTitle())
            .announce(getAnnounce(post.getText()))
            .likeCount((int) post.getPostVotesList().stream().filter(postVotes -> postVotes.getValue() == 1).count())
            .disLikeCount((int) post.getPostVotesList().stream().filter(postVotes -> postVotes.getValue() == -1).count())
            .commentCount(post.getPostCommentsList().size())
            .viewCount(post.getViewCount())
            .build())
        .collect(Collectors.toList());
  }

  public List<RsPostDto> getPostsByDate(String dateString) {
    return postRepository.getPostsByDate(dateString).stream()
        .map(post -> RsPostDto.builder()
            .id(post.getId())
            .timestamp(post.getTime().getTime()/1000)
            .user(RsUserDto.builder().id(post.getUser().getId()).name(post.getUser().getName()).build())
            .title(post.getTitle())
            .announce(getAnnounce(post.getText()))
            .likeCount((int) post.getPostVotesList().stream().filter(postVotes -> postVotes.getValue() == 1).count())
            .disLikeCount((int) post.getPostVotesList().stream().filter(postVotes -> postVotes.getValue() == -1).count())
            .commentCount(post.getPostCommentsList().size())
            .viewCount(post.getViewCount())
            .build())
        .collect(Collectors.toList());
  }

  public List<RsPostDto> getPostsByTag(String tagName) {
    Tag tag = tagRepository.findFirstByName(tagName).orElse(null);
    if (tag == null)
      return new ArrayList<>();
    return tag.getTagToPostList().stream().map(TagToPost::getPost)
        .map(post -> RsPostDto.builder()
            .id(post.getId())
            .timestamp(post.getTime().getTime()/1000)
            .user(RsUserDto.builder().id(post.getUser().getId()).name(post.getUser().getName()).build())
            .title(post.getTitle())
            .announce(getAnnounce(post.getText()))
            .likeCount((int) post.getPostVotesList().stream().filter(postVotes -> postVotes.getValue() == 1).count())
            .disLikeCount((int) post.getPostVotesList().stream().filter(postVotes -> postVotes.getValue() == -1).count())
            .commentCount(post.getPostCommentsList().size())
            .viewCount(post.getViewCount())
            .build())
        .collect(Collectors.toList());
  }

  public Post getPostById(Integer id) {
    return null;
  }

  private String getAnnounce(String text) {
    return text.replaceAll(MARKUP_REMOVE, "").substring(0, Math.min(text.length(), 150)) + "...";
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
