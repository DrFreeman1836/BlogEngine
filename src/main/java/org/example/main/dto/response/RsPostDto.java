package org.example.main.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RsPostDto {

  private Integer id;

  private Long timestamp;

  private RsUserDto user;

  private String title;

  private String announce;

  private Integer likeCount;

  private Integer disLikeCount;

  private Integer commentCount;

  private Integer viewCount;

}
