//package vimedia.service.ReportApp.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.time.ZonedDateTime;
//import java.util.Date;
//
//@Component
//public class JWTUtil {
//
//    @Value("${jwt_secret}")
//    private String secret;
//
//    public String generateToken(String username) {
//        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
//
//        return JWT.create()
//                .withSubject("User details")
//                .withClaim("username", username)
//                .withIssuedAt(new Date())
//                .withIssuer("ServiceApp")
//                .withExpiresAt(expirationDate)
//                .sign(Algorithm.HMAC256(secret));
//    }
//
//    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
//                .withSubject("User details")
//                .withIssuer("ServiceApp")
//                .build();
//
//        DecodedJWT jwt = jwtVerifier.verify(token);
//        return jwt.getClaim("username").asString();
//    }
//}
