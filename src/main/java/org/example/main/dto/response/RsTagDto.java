package org.example.main.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RsTagDto {

    private String name;

    private BigDecimal weight;

    public static RsTagDto tagDtoBuilder(String name, BigDecimal weight) {
        return new RsTagDto(name, weight);
    }


}
