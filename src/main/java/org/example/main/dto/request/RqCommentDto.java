package org.example.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RqCommentDto {

  @JsonProperty("parent_id")
  private Integer parentId;

  @JsonProperty("post_id")
  private Integer postId;

  private String text;

}
