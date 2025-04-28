package com.skilladmin.config;

import java.io.IOException;

import com.skilladmin.model.TrainingUsers;
import com.skilladmin.service.TrainingUserService;
import com.skilladmin.serviceImpl.TrainingUserSerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.skilladmin.serviceImpl.UserServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class Jwtvalidator extends OncePerRequestFilter {
    
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UserServiceImpl userService;
    private final TrainingUserSerImpl trainingUserService;

    @Autowired
    public Jwtvalidator(@Lazy  TrainingUserSerImpl trainingUserService) {
        this.trainingUserService = trainingUserService;
    }
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

	
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();

        if (requestPath.startsWith("/newskill") && !requestPath.startsWith("/newskill/api")) {
            // Continue the filter chain without performing authentication for /newskill/** but not /newskill/api/**
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String userEmail = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            userEmail = jwtUtils.extractUsername(jwtToken);
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;

            try {
                // First try to load user from userService
                userDetails = userService.loadUserByUsername(userEmail);
                if(userDetails==null){
                    userDetails = trainingUserService.loadUserByUsername(userEmail);

                }
            } catch (UsernameNotFoundException e) {
                // User not found in userService, try the trainingUserService

            }

            if (userDetails != null && jwtUtils.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
