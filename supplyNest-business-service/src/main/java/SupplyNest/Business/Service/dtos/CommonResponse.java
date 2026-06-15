package SupplyNest.Business.Service.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponse {
    private Integer status;
    private String message;
    private Object data;

    public CommonResponse(Integer status, String message){
        this.status = status;
        this.message = message;
    }
}
