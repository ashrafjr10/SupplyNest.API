package SupplyNest.Common.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
public class CommonResponse {
    @NotNull
    private Integer status;
    private String message;
    private Object data;

    public CommonResponse(Integer status, String message){
        this.status = status;
        this.message = message;
    }

    public CommonResponse(Integer status, String message, Object data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
