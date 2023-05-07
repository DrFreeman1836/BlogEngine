package org.example.main.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RsGlobalSettingsDto {

  @JsonProperty("MULTIUSER_MODE")
  private Boolean multiuserMode;

  @JsonProperty("POST_PREMODERATION")
  private Boolean postPremoderation;

  @JsonProperty("STATISTICS_IS_PUBLIC")
  private Boolean statisticsIsPublic;

}
