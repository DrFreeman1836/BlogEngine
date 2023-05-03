package org.example.main.model;

import java.util.Date;
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
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  /**
   * статус модерации
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "moderation_status", columnDefinition = "ENUM('NEW', 'ACCEPTED', 'DECLINED')", nullable = false)
  @ColumnDefault("NEW")
  private ModerationStatus moderationStatus;

  /**
   * id пользователя-модератора
   */
  @Column(name = "moderator_id")
  private Integer moderatorId;

  /**
   * автор поста
   */
  @Column(name = "user_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  /**
   * время публикации
   */
  @Column(name = "time", columnDefinition = "datetime", nullable = false)
  private Date time;

  /**
   * заголовок поста
   */
  @Column(name = "title", columnDefinition = "varchar(255)", nullable = false)
  private String title;

  /**
   * текст публикации
   */
  @Column(name = "text", columnDefinition = "text", nullable = false)
  private String text;

  /**
   * кол-во просмотров
   */
  @Column(name = "view_count", nullable = false)
  @ColumnDefault("0")
  private Integer viewCount;

}
