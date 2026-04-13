package ch.zhaw.freelancer4u.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Instant;
import java.util.List;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(testJwtDecoder())));
        return http.build();
    }

    @Bean
    @Primary
    public JwtDecoder testJwtDecoder() {
        return token -> Jwt.withTokenValue(token)
            .header("alg", "none")
            .claim("sub", "testuser")
            .claim("user_roles", List.of("admin"))
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();
    }
}
