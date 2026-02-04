package com.camicompany.auth_service.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class JwtUtils {

        @Value("${spring.security.jwt.private.key}")
        private String privateKey;

        @Value("${spring.security.jwt.user.generator}")
        private String userGenerator;

        public String createToken (Authentication authentication){
            //algorith to use
            Algorithm algorithm = Algorithm.HMAC256(privateKey);

            //retrieve user roles and permissions
            String username = authentication.getPrincipal().toString();

            String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            //create the token
            String jwtToken = JWT.create()
                    .withIssuer(userGenerator)
                    .withSubject(username)
                    .withClaim("authorities", authorities)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + (30*60000))) //The token lasts 30 minutes
                    .withNotBefore(new Date(System.currentTimeMillis()))
                    .sign(algorithm);
            return jwtToken;

        }

        public DecodedJWT validateToken (String token){
            try {
                //get the algorithm used to encrypt and create the verifier
                Algorithm algorithm = Algorithm.HMAC256(privateKey);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer(userGenerator)
                        .build();


                DecodedJWT decodedJWT = verifier.verify(token);
                return decodedJWT;
            }
            catch (JWTVerificationException exception){
                throw new JWTVerificationException("Invalid token. Not authenticated");
            }
        }

        //method to get the username from the token
        public String extractUsername (DecodedJWT decodedJWT){
            return decodedJWT.getSubject();
        }

        //method to get a specific claim from the token
        public Claim getSpecificClaim (DecodedJWT decodedJWT, String claimName){

            return decodedJWT.getClaim(claimName);
        }

        //method to get all claims from the token
        public Map<String, Claim> returnAllClaims (DecodedJWT decodedJWT){
            return decodedJWT.getClaims();
        }

}
