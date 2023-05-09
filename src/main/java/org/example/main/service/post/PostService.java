package org.example.main.service.post;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.response.RsPostDto;
import org.example.main.dto.response.RsUserDto;
import org.example.main.model.ModerationStatus;
import org.example.main.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

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

  private String getAnnounce(String text) {
    return text.replaceAll("/<[^>]+>/gi", "").substring(0, Math.min(text.length(), 150)) + "...";
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
