package security;

import io.jsonwebtoken.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;



public class JwtAuthenticationFilter
        extends OncePerRequestFilter {


    private final String SECRET_KEY =
            "rikkei_secret_key_super_secure_do_not_share";




    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )
            throws ServletException, IOException {



        String authHeader =
                request.getHeader("Authorization");



        try {


            if(authHeader != null
                    && authHeader.startsWith("Bearer ")) {



                String token =
                        authHeader.substring(7);



                String username =
                        Jwts.parser()
                                .setSigningKey(SECRET_KEY)
                                .parseClaimsJws(token)
                                .getBody()
                                .getSubject();



                if(username != null
                        &&
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                == null){



                    /*
                     *
                     * Logic set Authentication
                     *
                     * UsernamePasswordAuthenticationToken
                     *
                     * SecurityContextHolder.setContext()
                     *
                     */



                }

            }



            /*
             *
             * Token OK
             * tiếp tục request
             *
             */

            filterChain.doFilter(
                    request,
                    response
            );



        }



        catch(ExpiredJwtException ex){



            SecurityContextHolder
                    .clearContext();



            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );


            response.setContentType(
                    "application/json"
            );



            response.getWriter()
                    .write(
                            """
                            {
                              "status":401,
                              "code":"JWT_EXPIRED",
                              "message":"JWT token expired"
                            }
                            """
                    );



        }



        catch(JwtException ex){



            SecurityContextHolder
                    .clearContext();



            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );



            response.setContentType(
                    "application/json"
            );



            response.getWriter()
                    .write(
                            """
                            {
                              "status":401,
                              "code":"JWT_INVALID",
                              "message":"JWT token invalid"
                            }
                            """
                    );

        }



    }


}