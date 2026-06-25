bạn là 1 chuyên gia IT với 10 năm kinh nghiệm  Khi JWT hết hạn, service sẽ ném ExpiredJwtException thay vì trả về HTTP 401, service lại crash và trả về HTTP 500 code hiện tại
package com.rikkei.security;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

private final String SECRET_KEY = "rikkei_secret_key_super_secure_do_not_share";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
           
            String username = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
                    
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Logic set Authentication vào SecurityContext (đã rút gọn)
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
và log lỗi:
io.jsonwebtoken.ExpiredJwtException: JWT expired at 2026-06-17T10:00:00Z. Current time: 2026-06-17T10:05:00Z
at io.jsonwebtoken.impl.DefaultJwtParser.parse(DefaultJwtParser.java:381)
at com.rikkei.security.JwtAuthenticationFilter.doFilterInternal(JwtAuthenticationFilter.java:45)
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)

hãy phân tích: 
- Nguyên nhân gốc rễ tại sao filter ném exception dẫn đến 500
- Tại sao Global Exceptionhandler (@controller) không bắt được exception từ filter 
- Đưa ra giải pháp tối ưu nhất để xử lý tất cả các authentication

5. Kiến trúc xử lý Authentication chuẩn Production

Tôi thường thiết kế flow như sau:

                 Request

                    |
                    v

          JwtAuthenticationFilter

                    |
        +-----------+-----------+
        |                       |
     Valid JWT              Invalid JWT
        |                       |
        v                       v

SecurityContext        AuthenticationEntryPoint

        |                       |
        v                       v

Controller             HTTP 401 JSON

        |
        v

Service

        |
        v

Business Exception

        |
        v

GlobalExceptionHandler

        |
        v

HTTP 400/404/409/500