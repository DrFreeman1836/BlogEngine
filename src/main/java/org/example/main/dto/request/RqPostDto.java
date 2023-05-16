package org.example.main.dto.request;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RqPostDto {

  private Date timestamp;

  private Integer active;

  private String title;

  private List<String> tags;

  private String text;


}
