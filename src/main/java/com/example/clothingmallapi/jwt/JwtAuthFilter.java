package com.example.clothingmallapi.jwt;

import com.example.clothingmallapi.security.SecurityExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // 토큰이 필요 없는 URL 패턴==============================================
    private static final String[] FILTER_IGNORE_URLS = {"/login","/signup"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);
        // GET 요청, login, signup 요청에 대해서는 필터를 거치지 않음. ==========================
        String requestURI = request.getRequestURI();
        String requstMethod = request.getMethod();
        String ignoreHttpMethod = "GET";
        String requestPathForFilter = "/wishInventories";

        if(ignoreHttpMethod.equalsIgnoreCase(requstMethod)&&!requestURI.contains(requestPathForFilter)){
            filterChain.doFilter(request,response);
            return;
        }
        if(isIgnoredURL(requestURI)){
            filterChain.doFilter(request,response);
            return;
        }
        //===================================================================================


        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                //handleTokenError(response, "Token Null error", HttpStatus.UNAUTHORIZED);
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
        }
        // Token Null Error 핸들링 =======================================================
        else{
            jwtExceptionHandler(response, "Token NULL error", HttpStatus.UNAUTHORIZED.value());
            return;
        }
        //================================================================================
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String emailId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(emailId);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private boolean isIgnoredURL(String requestURI) {
        for (String ignoredUrl : FILTER_IGNORE_URLS) {
            if (requestURI.endsWith(ignoredUrl)) {
                return true;
            }
        }
        return false;
    }


}
