package com.camicompany.shopping_cart_service.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JwtUtils {
    @Value("${spring.security.jwt.private.key}")
    private String privateKey;

    @Value("${spring.security.jwt.user.generator}")
    private String userGenerator;

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
