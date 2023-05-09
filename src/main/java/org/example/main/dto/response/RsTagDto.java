package org.example.main.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RsTagDto {

    private String name;

    private BigDecimal weight;


}
