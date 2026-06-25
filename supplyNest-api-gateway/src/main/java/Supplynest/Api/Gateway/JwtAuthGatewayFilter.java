package Supplynest.Api.Gateway;

import SupplyNest.Common.CurrentUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public int getOrder() {
        return -100; // High priority
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Gateway filter processing path: " + path);

        if (validator.predicate.test(exchange.getRequest())) {
            System.out.println("Path requires authentication: " + path);

            if (!exchange.getRequest().getHeaders()
                    .containsKey(HttpHeaders.AUTHORIZATION)) {

                throw new RuntimeException("Missing Authorization Header");
            }

            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Invalid Authorization Header");
            }

            String token = authHeader.substring(7);

            try {
                return authServiceClient.validateToken(token)
                        .flatMap(response -> {

                            if (!response.isValid()) {
                                return Mono.error(new RuntimeException("Invalid Token"));
                            }

                            CurrentUser user = response.getUser();

                            String roleJson;
                            try {
                                roleJson = objectMapper.writeValueAsString(user.getRole());
                            } catch (Exception e) {
                                return Mono.error(new RuntimeException("Failed to serialize role"));
                            }

                            ServerHttpRequest request = exchange.getRequest()
                                    .mutate()
                                    .header("X-User-Id", user.getUserId().toString())
                                    .header("X-Role", roleJson)
                                    .header("X-Business-Code", user.getBusinessCode())
                                    .header("X-Business-Group-Code", user.getBusinessGroupCode())
                                    .header("X-User-Type", user.getUserType())
                                    .build();

                            return chain.filter(exchange.mutate().request(request).build()
                            );
                        });
            } catch (Exception e) {
                throw new RuntimeException("Invalid Token");
            }
        }

        System.out.println("Path is open (no auth required): " + path);
        return chain.filter(exchange)
                .doOnSuccess(v -> System.out.println("Request completed successfully for path: " + path))
                .doOnError(e -> System.out.println("Request failed for path: " + path + ", error: " + e.getMessage()));
    }
}