package de.effitient.dev.hashicorpvaultpoc.configuration;

import de.effitient.dev.hashicorpvaultpoc.authentication.ApiKeyAuthenticationFilter;
import de.effitient.dev.hashicorpvaultpoc.authentication.ApiKeyAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final ApiKeyAuthenticationProvider apiKeyAuthenticationProvider;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        enableH2Console(http);

        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/actuator/health").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new ApiKeyAuthenticationFilter("/api/**", authenticationManager()), AnonymousAuthenticationFilter.class)
            .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    private void enableH2Console(final HttpSecurity http) throws Exception {
        if (environment.acceptsProfiles(Profiles.of(ProfileConstants.STAGING, ProfileConstants.PRODUCTION))) {
            http.authorizeRequests()
                .antMatchers("/h2-console/**").permitAll();

            http.headers()
                .frameOptions().sameOrigin();
        }
    }

    @Override
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(apiKeyAuthenticationProvider));
    }
}
