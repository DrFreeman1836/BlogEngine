package org.example.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.main.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RsUserDtoWithPhoto extends RsUserDto {

  private String photo;

  @Override
  public void fillFields(User user) {
    super.fillFields(user);
    this.photo = user.getPhoto();
  }
}
