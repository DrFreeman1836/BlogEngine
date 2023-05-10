package org.example.main.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.main.model.PostComments;

@Getter
@Setter
public class RsCommentDto {

  private Integer id;
  private Long timestamp;
  private String text;
  private RsUserDtoWithPhoto user;

  public void fillFields(PostComments comments) {
    this.id = comments.getId();
    this.timestamp = comments.getTime().getTime() / 1000;
    this.text = comments.getText();
    RsUserDtoWithPhoto userDto = new RsUserDtoWithPhoto();
    userDto.fillFields(comments.getUser());
    this.user = userDto;
  }

}
