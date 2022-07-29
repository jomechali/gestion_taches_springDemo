package com.diginamic.gt.security;

import com.diginamic.gt.entities.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenUtils {

    public String generateToken(Employee employee) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", employee.getFirstName());
        claims.put("lastName", employee.getLastName());
        claims.put("userName", employee.getUsername());
        claims.put("roles", employee.getRoles());

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR, 2);

        String token = Jwts.builder()
                .setClaims(claims) // user info
                .setSubject(employee.getUsername()) // identificate user
                .setIssuer("GESTIONS_TACHES") // projet qui genere
                .setIssuedAt(now) // creation date
                .setExpiration(calendar.getTime()) // mandatory
                .signWith(SignatureAlgorithm.HS256, "SECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEY")
                .compact(); // turn into string

        return token;
    }

    public String getUserNameFromToken(String token) {
        Claims claims = getBody(token);
        return claims.getSubject(); // le username dans le token, par ailleurs c est une map
    }

    private Claims getBody(String token) {
        return Jwts.parserBuilder().build().setSigningKey("SECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEYSECRETKEY")
                .parseClaimsJwt(token).getBody();
    }

    public boolean isTokenValid(String token, UserDetails employee) {
        Claims claims = getBody(token);
        Date expiration = claims.getExpiration();
        String userNameFromToken = getUserNameFromToken(token);
        Boolean isValidUserName = userNameFromToken.equals(employee.getUsername());
        Boolean isValidDate = expiration.before(new Date());
        return isValidUserName && isValidDate;
    }
}
