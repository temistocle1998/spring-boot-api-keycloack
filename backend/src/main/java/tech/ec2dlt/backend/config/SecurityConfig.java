package tech.ec2dlt.backend.config;

import java.io.IOException;
import java.util.Arrays;

import javax.naming.AuthenticationException;

/*
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.core.convert.converter.Converter; import
 * org.springframework.security.authentication.AbstractAuthenticationToken;
 * import org.springframework.security.config.annotation.method.configuration.
 * EnableMethodSecurity; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.config.annotation.web.configurers.
 * AbstractHttpConfigurer; import
 * org.springframework.security.config.http.SessionCreationPolicy; import
 * org.springframework.security.oauth2.jwt.Jwt; import
 * org.springframework.security.web.SecurityFilterChain; import
 * org.springframework.web.cors.CorsConfiguration; import
 * org.springframework.web.cors.CorsConfigurationSource; import
 * org.springframework.web.cors.UrlBasedCorsConfigurationSource;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity
 * 
 * @EnableMethodSecurity public class SecurityConfig {
 * 
 * @Autowired private Converter<Jwt, ? extends AbstractAuthenticationToken>
 * jwtConverter;
 * 
 * @Bean public CorsConfigurationSource corsConfigurationSource() {
 * CorsConfiguration configuration = new CorsConfiguration();
 * configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
 * configuration.setAllowedMethods(Arrays.asList("GET","POST"));
 * UrlBasedCorsConfigurationSource source = new
 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
 * configuration); return source; }
 * 
 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
 * throws Exception {
 * 
 * http.csrf(AbstractHttpConfigurer::disable) .authorizeHttpRequests((authorize)
 * -> authorize.anyRequest().authenticated()) .oauth2ResourceServer((oauth2) ->
 * oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)))
 * .sessionManagement(session ->
 * session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
 * .authorizeHttpRequests(req -> req.anyRequest().permitAll());
 * 
 * return http.build(); }
 * 
 * }
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.ec2dlt.backend.ApiResponse;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtConverter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8001"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	 http.csrf(AbstractHttpConfigurer::disable);
         http.authorizeRequests()
                 .requestMatchers("/api/login").permitAll()
                 .anyRequest().authenticated();
         http.oauth2ResourceServer()
                 .jwt()
                 .jwtAuthenticationConverter(jwtConverter);
         http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
         return http.build();
    }
    
    // Custom unauthenticated handler method using ApiResponse<T>
    private void handleUnauthenticated(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Create the ApiResponse for unauthenticated access
        ApiResponse<String> apiResponse = new ApiResponse<>(true, "Unauthenticated");

        // Set response status and headers
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}
