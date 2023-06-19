package com.example.UserService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class CustomauthorizationFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(CustomauthorizationFilter.class);
//    implements methods
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationString = request.getHeader("Authorization");
        if(authorizationString == null){
            logger.warn("No Token!");
        }
        else {
            if(authorizationString.startsWith("Bearer ")){
            try{
                String token = authorizationString.substring("Bearer ".length());
                Algorithm algo = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier= JWT.require(algo).build();
                DecodedJWT decode = verifier.verify(token);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                String[] roles = decode.getClaim("role").asArray(String.class);
                for(String role: roles){
                    authorities.add(new SimpleGrantedAuthority(role));
                }
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(decode.getSubject(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request,response);
            }catch (Exception e){
                logger.warn(e.getMessage());
            }

            }else{
                logger.warn("not bearer");
            }
        }
    }
}
