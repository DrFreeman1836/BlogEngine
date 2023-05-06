package org.example.main.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "captcha_codes")
public class CaptchaCodes {

  /**
   * id каптча
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * дата генерации капчи
   */
  private Date time;

  /**
   * код, отображаемый на картинке
   */
  @Column(columnDefinition = "tinytext")
  private String code;

  /**
   * код, передаваемый в параметре
   */
  @Column(columnDefinition = "tinytext")
  private String secretCode;

}
