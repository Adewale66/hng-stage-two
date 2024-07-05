package com.hngstagetwo.jwt;

import com.hngstagetwo.users.UsersRepository;
import com.hngstagetwo.users.UsersService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UsersRepository usersRepository;
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(@Nonnull  HttpServletRequest request,
                                    @Nonnull  HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {
        String auth = request.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = auth.substring(7);
        final String username = jwtService.extractUsername(jwt);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (username != null && authentication == null) {
            UserDetails userDetails = usersRepository.findByEmail(username).orElseThrow();

            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(token);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);

    }
}
