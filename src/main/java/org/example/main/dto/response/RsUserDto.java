package org.example.main.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RsUserDto {

  private Integer id;

  private String name;

}
