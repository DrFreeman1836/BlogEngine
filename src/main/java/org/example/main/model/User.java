package org.example.main.model;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "users")
public class User {

  /**
   * id пользователя
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * признак модератора
   */
  @Column(columnDefinition = "tinyint")
  private Boolean isModerator;

  /**
   * дата и время регистрации пользователя
   */
  private Date regTime;

  /**
   * имя пользователя
   */
  private String name;

  /**
   * e-mail пользователя
   */
  private String email;

  /**
   * хэш пароля пользователя
   */
  private String password;

  /**
   * код восстановления пароля
   */
  private String code;

  /**
   * фотография
   */
  @Column(columnDefinition = "text")
  private String photo;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<Post> listPosts;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<PostVotes> postVotesList;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<PostComments> postCommentsList;

}
