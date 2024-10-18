package tech.ec2dlt.backend.config;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.ec2dlt.backend.ApiResponse;

import org.springframework.security.core.AuthenticationException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt().jwtAuthenticationConverter(jwtConverter)
                )
                .exceptionHandling((exception) ->
                        exception.authenticationEntryPoint(unauthenticatedHandler())
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    private AuthenticationEntryPoint unauthenticatedHandler() {
        return (request, response, authException) -> handleUnauthenticated(request, response, authException);
    }

    private void handleUnauthenticated(HttpServletRequest request, HttpServletResponse response,
                                       AuthenticationException authException) throws IOException {
        // Create the ApiResponse for unauthenticated access
        ApiResponse<String> apiResponse = new ApiResponse<>(true, "unauthenticated");

        // Set response status and headers
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        // Write the response body
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));

        // Ensure the response is properly flushed and closed
        response.getWriter().flush();
        response.getWriter().close();
    }

}
