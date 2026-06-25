package SupplyNest.Common.dtos;

import SupplyNest.Common.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {

    private boolean isValid;
    private CurrentUser user;
}
