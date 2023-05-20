package org.example.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RqModerationStatus {

  @JsonProperty("post_id")
  private Integer postId;

  private String decision;

}
