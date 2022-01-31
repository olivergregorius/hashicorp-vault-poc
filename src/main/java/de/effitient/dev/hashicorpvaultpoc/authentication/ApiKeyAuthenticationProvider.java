package de.effitient.dev.hashicorpvaultpoc.authentication;

import de.effitient.dev.hashicorpvaultpoc.configuration.properties.AuthenticationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationProperties authenticationProperties;

    public ApiKeyAuthenticationProvider(final AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String apiKey = (String) authentication.getPrincipal();
        if (StringUtils.isEmpty(apiKey)) {
            log.error("No API key was provided in the request");
            throw new InsufficientAuthenticationException("No API key was provided in the request");
        } else {
            if (authenticationProperties.getValidApiKeys().contains(apiKey)) {
                return new ApiKeyAuthenticationToken(apiKey, true);
            }
            log.error("API key is invalid");
            throw new BadCredentialsException("API key is invalid");
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
