package patil.parshwa.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import patil.parshwa.blog.models.User;
import patil.parshwa.blog.repositories.UserRepository;

import java.io.IOException;

@Component
@Slf4j // logging
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired private UserRepository userRepository;
    @Autowired private  AuthUtil authUtil;
    @Autowired private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Inside JwtAuthFilter - doFilterInternal");
        log.info("Request URI: {}", request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = authHeader.substring(7);
            String username = authUtil.getUsernameFromToken(jwt);

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                User user  = userRepository.findByUsername(username).orElseThrow(() ->
                        new RuntimeException("User not found with username: " + username)
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error("Exception in JwtAuthFilter: ", ex);
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
