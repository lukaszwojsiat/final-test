package pl.kurs.finaltest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("employee")
                .password(passwordEncoder.encode("employee"))
                .roles("EMPLOYEE")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("EMPLOYEE", "IMPORTER", "ADMIN")
                .build();

        UserDetails importer = User.withUsername("importer")
                .password(passwordEncoder.encode("importer"))
                .roles("EMPLOYEE", "IMPORTER")
                .build();

        return new InMemoryUserDetailsManager(user, admin, importer);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, "/api/persons").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/persons").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/persons/upload").hasAnyRole("IMPORTER")
                        .requestMatchers(HttpMethod.POST, "/api/persons/{id}/position").hasAnyRole("EMPLOYEE")
                        .anyRequest().authenticated())
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }
}