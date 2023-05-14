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
public class RsUserDto {

  private Integer id;

  private String name;

  public void fillFields(User user) {
    this.id = user.getId();
    this.name = user.getName();
  }

  public static RsUserDto userDtoBuilder(Integer id, String name) {
    return new RsUserDto(id, name);
  }

}
