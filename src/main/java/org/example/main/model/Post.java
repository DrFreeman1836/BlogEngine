package org.example.main.model;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post {

  /**
   * id поста
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * признак активности публикации
   */
  @Column(columnDefinition = "tinyint")
  private Boolean isActive;

  /**
   * статус модерации
   */
  @Column(columnDefinition = "enum('new', 'accepted', 'declined')")
  private ModerationStatus moderationStatus;

  /**
   * id пользователя-модератора
   */
  private Integer moderatorId;

  /**
   * автор поста
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  /**
   * время публикации
   */
  private Date time;

  /**
   * заголовок поста
   */
  private String title;

  /**
   * текст публикации
   */
  @Column(columnDefinition = "text")
  private String text;

  /**
   * кол-во просмотров
   */
  private Integer viewCount;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<PostVotes> postVotesList;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<TagToPost> tagToPostList;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<PostComments> postCommentsList;

}
