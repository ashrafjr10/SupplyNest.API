package SupplyNest.Common.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    @NotNull
    private Integer status;
    private String message;
    private Object data;
}
