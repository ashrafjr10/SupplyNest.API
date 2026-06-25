package Supplynest.Api.Gateway;

import SupplyNest.Common.dtos.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceClient {

    private final WebClient.Builder webClientBuilder;

    public Mono<TokenValidationResponse> validateToken(String token) {

        return webClientBuilder.build().get()
                .uri("http://SUPPLYNEST-AUTH-SERVICE/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(TokenValidationResponse.class);
    }
}
