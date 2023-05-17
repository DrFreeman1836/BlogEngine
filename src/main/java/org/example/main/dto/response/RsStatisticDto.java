package org.example.main.dto.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RsStatisticDto {

  private Integer postsCount;

  private Integer likesCount;

  private Integer dislikesCount;

  private Integer viewsCount;

  private Long firstPublication;

}
