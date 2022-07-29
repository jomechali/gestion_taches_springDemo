package com.diginamic.gt.security;

import com.diginamic.gt.entities.Employee;
import com.diginamic.gt.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    private AccountService accountService;
    private JWTTokenUtils jwtTokenUtils;
    private String authorization;

    // pas en lombok, car se fait au runtime?
    public JWTFilter(AccountService accountService, JWTTokenUtils jwtTokenUtils, String authorization) {
        this.accountService = accountService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authorization = authorization;
    }

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(authorization); //nom du champs qui contient le token
        if (header != null && header.startsWith("Bearer ")) { // nom dans la requete
            String token = header.substring(7);
            String userName = jwtTokenUtils.getUserNameFromToken(token);

            if(userName != null){
                // employee extends userdetails
                UserDetails employee = this.accountService.loadUserByUsername(userName); // to make sure we use the data from db
                boolean isTokenValid = jwtTokenUtils.isTokenValid(token, employee);

                if (isTokenValid) { //here the check for the role, does the user has the right
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(employee, null, employee.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    // il y a une premiere verif avec l url et le jeton dans les config securite
                    // et ici on fait les check avec les infos DB

                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
