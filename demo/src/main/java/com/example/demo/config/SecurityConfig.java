package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain setFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Client(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/role/visiter")).hasRole("ROLE_VISITER")

                        .requestMatchers(new AntPathRequestMatcher("/index/**")).permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration dummyRegistration = ClientRegistration.withRegistrationId("keycloak-oauth2")
                .clientId("dummy-client-id")
                .clientSecret("dummy-client-secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("https://localhost:8443/login/oauth2/code/keycloak")
                .issuerUri("https://keycloak.example.com/realms/myrealm")
                .authorizationUri("https://keycloak.example.com/realms/myrealm/protocol/openid-connect/auth")
                .scope("openid", "email", "profile", "roles")
                .tokenUri("https://keycloak.example.com/realms/myrealm/protocol/openid-connect/token")
                .userInfoUri("https://keycloak.example.com/realms/myrealm/protocol/openid-connect/userinfo")
                .jwkSetUri("https://keycloak.example.com/realms/myrealm/protocol/openid-connect/certs")
                .build();
        return new InMemoryClientRegistrationRepository(dummyRegistration);
    }

}
