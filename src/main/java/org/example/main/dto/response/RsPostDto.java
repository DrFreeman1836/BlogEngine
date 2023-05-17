package org.example.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.main.model.Post;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

  private static final String MARKUP_REMOVE = "\\<.*?>";

  public void fillFields(Post post) {
    this.id = post.getId();
    this.timestamp = post.getTime().getTime() / 1000;
    this.user = RsUserDto.userDtoBuilder(post.getUser().getId(), post.getUser().getName());
    this.title = post.getTitle();
    this.announce = getAnnounce(post.getText());
    this.likeCount = (int) post.getPostVotesList().stream().filter(postVotes -> postVotes.getValue() == 1).count();
    this.disLikeCount = (int) post.getPostVotesList().stream().filter(postVotes -> postVotes.getValue() == -1).count();
    this.commentCount = post.getPostCommentsList().size();
    this.viewCount = post.getViewCount();
  }

  private String getAnnounce(String text) {
    String textOutTag = text.replaceAll(MARKUP_REMOVE, "");
    return textOutTag.substring(0, Math.min(textOutTag.length(), 150)) + "...";
  }

}
