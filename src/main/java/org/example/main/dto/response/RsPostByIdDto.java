package org.example.main.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.main.model.Post;
import org.example.main.model.Tag;
import org.example.main.model.TagToPost;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RsPostByIdDto extends RsPostDto {

  private Boolean active;

  private String text;

  private List<RsCommentDto> comments;

  private List<String> tags;

  @Override
  public void fillFields(Post post) {
    super.fillFields(post);
    this.active = post.getIsActive();
    this.text = post.getText();
    this.comments = post.getPostCommentsList().stream().map(c -> {
      RsCommentDto commentDto = new RsCommentDto();
      commentDto.fillFields(c);
      return commentDto;
    }).collect(Collectors.toList());
    this.tags = post.getTagToPostList().stream().map(TagToPost::getTag).map(Tag::getName).collect(
        Collectors.toList());
  }

}
