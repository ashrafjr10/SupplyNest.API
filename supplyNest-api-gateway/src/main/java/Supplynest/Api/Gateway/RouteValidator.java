package Supplynest.Api.Gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/validate",
            "/eureka",
            "/auth-service/v3/api-docs",
            "/business-service/v3/api-docs",
            "/file-service/v3/api-docs",
            "/catalog-service/v3/api-docs",
            "/swagger-ui",
            "/swagger-ui.html",
            "/v3/api-docs"
    );

    public Predicate<ServerHttpRequest> predicate =
            request -> openApiEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
