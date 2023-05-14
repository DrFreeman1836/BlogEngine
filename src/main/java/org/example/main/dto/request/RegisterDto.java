package org.example.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

  @JsonProperty("e_mail")
  private String email;

  private String name;

  private String password;

  private String captcha;

  @JsonProperty("captcha_secret")
  private String captchaSecret;

}
