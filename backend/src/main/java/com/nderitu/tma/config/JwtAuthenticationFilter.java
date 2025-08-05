package com.nderitu.tma.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.nderitu.tma.token.TokenRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (request.getServletPath().contains("/api/auth")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      log.debug("No Authorization header or not Bearer token for path: {}", request.getServletPath());
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    userEmail = jwtService.extractUsername(jwt);
    log.debug("JWT processing for user: {} on path: {}", userEmail, request.getServletPath());
    
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        var isTokenValid = tokenRepository.findByToken(jwt)
            .map(t -> !t.isExpired() && !t.isRevoked())
            .orElse(false);
        
        log.debug("User loaded: {}, authorities: {}, token valid: {}", 
                 userDetails.getUsername(), userDetails.getAuthorities(), isTokenValid);
        
        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
          );
          authToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request)
          );
          SecurityContextHolder.getContext().setAuthentication(authToken);
          log.debug("Authentication set successfully for user: {}", userEmail);
        } else {
          log.debug("Token validation failed for user: {}", userEmail);
        }
      } catch (Exception e) {
        log.error("Error processing JWT for user {}: {}", userEmail, e.getMessage());
      }
    } else {
      log.debug("Skipping JWT processing - userEmail: {}, existing auth: {}", 
               userEmail, SecurityContextHolder.getContext().getAuthentication() != null);
    }
    filterChain.doFilter(request, response);
  }
}
