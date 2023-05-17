package org.example.main.service.statistic;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.response.RsPostDto;
import org.example.main.dto.response.RsStatisticDto;
import org.example.main.service.auth.AuthService;
import org.example.main.service.post.ModeSorting;
import org.example.main.service.post.PostService;
import org.example.main.service.post.StatusMode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticService {

  private final PostService postService;

  private final AuthService authService;

  public RsStatisticDto getMyStatistic(Principal principal) {
    List<RsPostDto> myPosts = postService.getMyPost(StatusMode.published, principal);
    return calculate(myPosts);
  }

  public RsStatisticDto getAllStatistic() {
    List<RsPostDto> allPosts = postService.getPosts(ModeSorting.recent);
    return calculate(allPosts);
  }

  private RsStatisticDto calculate(List<RsPostDto> listPosts) {
    RsStatisticDto statisticDto = new RsStatisticDto();
    statisticDto.setPostsCount(listPosts.size());
    statisticDto.setLikesCount(listPosts.stream().mapToInt(RsPostDto::getLikeCount).sum());
    statisticDto.setDislikesCount(listPosts.stream().mapToInt(RsPostDto::getDisLikeCount).sum());
    statisticDto.setViewsCount(listPosts.stream().mapToInt(RsPostDto::getViewCount).sum());
    statisticDto.setFirstPublication(listPosts.stream().mapToLong(RsPostDto::getTimestamp).min().orElse(0));
    return statisticDto;
  }

}
