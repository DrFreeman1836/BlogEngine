package org.example.main.dto.response;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class RsInfoBlogDto {

  @Value("${application.title}")
  private String title;

  @Value("${application.subtitle}")
  private String subtitle;

  @Value("${application.phone}")
  private String phone;

  @Value("${application.email}")
  private String email;

  @Value("${application.copyright}")
  private String copyright;

  @Value("${application.copyrightFrom}")
  private String copyrightFrom;

}
