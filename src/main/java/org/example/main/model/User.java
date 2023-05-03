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
  @Column(name = "is_moderator", nullable = false)
  private Boolean isModerator;

  /**
   * дата и время регистрации пользователя
   */
  @Column(name = "reg_time", columnDefinition = "datetime", nullable = false)
  private Date regTime;

  /**
   * имя пользователя
   */
  @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
  private String name;

  /**
   * e-mail пользователя
   */
  @Column(name = "email", columnDefinition = "varchar(255)", nullable = false)
  private String email;

  /**
   * хэш пароля пользователя
   */
  @Column(name = "password", columnDefinition = "varchar(255)", nullable = false)
  private String password;

  /**
   * код восстановления пароля
   */
  @Column(name = "code", columnDefinition = "varchar(255)")
  private String code;

  /**
   * фотография
   */
  @Column(name = "photo", columnDefinition = "text")
  private String photo;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<Post> listPosts;

}
