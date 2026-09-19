package com.rider.companion.config;

import com.rider.companion.repository.RevokedTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final RevokedTokenRepository revokedTokens;

  public JwtAuthenticationFilter(JwtService jwtService, RevokedTokenRepository revokedTokens) {
    this.jwtService = jwtService;
    this.revokedTokens = revokedTokens;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    try {
      JwtService.Claims claims = jwtService.verify(header.substring(7));
      if (revokedTokens.existsById(claims.jti()))
        throw new IllegalArgumentException("Token revoked");
      SecurityContextHolder.getContext()
          .setAuthentication(
              new UsernamePasswordAuthenticationToken(claims.userId(), null, List.of()));
      filterChain.doFilter(request, response);
    } catch (IllegalArgumentException exception) {
      SecurityContextHolder.clearContext();
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write("{\"message\":\"Session expired or invalid\"}");
    }
  }
}
