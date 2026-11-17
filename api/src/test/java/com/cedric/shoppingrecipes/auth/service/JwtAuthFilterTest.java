package com.cedric.shoppingrecipes.auth.service;

import com.cedric.shoppingrecipes.auth.JwtAuthFilter;
import com.cedric.shoppingrecipes.auth.JwtService;
import com.cedric.shoppingrecipes.user.service.UserDetailsServiceImpl;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock private JwtService jwtService;
    @Mock private UserDetailsServiceImpl userDetailsService;

    @InjectMocks private JwtAuthFilter jwtAuthFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueWithoutAuthenticationWhenTokenIsMalformed() throws Exception {
        when(jwtService.extractUsername("bidon"))
                .thenThrow(new MalformedJwtException("token invalide"));

        MockFilterChain chain = runFilterWithToken("bidon");

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(chain.getRequest()).isNotNull();
    }

    @Test
    void shouldContinueWithoutAuthenticationWhenUserNoLongerExists() throws Exception {
        when(jwtService.extractUsername("token-valide"))
                .thenReturn("supprime@example.com");
        when(userDetailsService.loadUserByUsername("supprime@example.com"))
                .thenThrow(new UsernameNotFoundException("supprime@example.com"));

        MockFilterChain chain = runFilterWithToken("token-valide");

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(chain.getRequest()).isNotNull();
    }

    private MockFilterChain runFilterWithToken(String token) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/auth/me");
        request.setCookies(new Cookie("access_token", token));

        MockFilterChain chain = new MockFilterChain();
        jwtAuthFilter.doFilter(request, new MockHttpServletResponse(), chain);
        return chain;
    }
}