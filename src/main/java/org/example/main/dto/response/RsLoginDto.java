package org.example.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.main.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsLoginDto extends RsUserDtoWithPhoto {

  private String email;

  private Boolean moderation;

  private Integer moderationCount;

  private Boolean settings;

  @Override
  public void fillFields(User user) {
    super.fillFields(user);
    this.email = user.getEmail();
    this.moderation = user.getIsModerator();
    this.settings = user.getIsModerator();
  }

  public void fillFields(User user, Integer moderationCount) {
    fillFields(user);
    this.moderationCount = !user.getIsModerator() ? 0 : moderationCount;
  }
}