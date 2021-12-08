package toyproject.syxxn.back_end.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import toyproject.syxxn.back_end.exception.InvalidTokenException;
import toyproject.syxxn.back_end.security.auth.AuthDetails;
import toyproject.syxxn.back_end.security.auth.AuthDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${auth.jwt.secret}")
    private String secretKey;

    @Value("${auth.jwt.exp.access}")
    private Long accessTokenExpiration;

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshTokenExpiration;

    private static final String HEADER = "Authorization";

    private static final String PREFIX = "Bearer";

    private final AuthDetailsService authDetailsService;

    public String generateAccessToken(Integer id) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000))
                .setIssuedAt(new Date())
                .setHeaderParam("typ", "access_token")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setSubject(id.toString())
                .compact();
    }

    public String generateRefreshToken(Integer id) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000))
                .setIssuedAt(new Date())
                .setHeaderParam("typ", "refresh_token")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setSubject(id.toString())
                .compact();
    }

    public boolean isRefreshToken(String token) {
        try {
            return getHeader(token).get("typ").equals("refresh_token");
        } catch (Exception e) {
            throw InvalidTokenException.EXCEPTION;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER);
        if (bearerToken != null && bearerToken.startsWith(PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            return getBody(token).getExpiration().after(new Date());
        } catch (Exception e) {
            throw InvalidTokenException.EXCEPTION;
        }
    }

    public Authentication getAuthentication(String token) {
        AuthDetails authDetails = authDetailsService.loadUserByUsername(getId(token));
        return new UsernamePasswordAuthenticationToken(authDetails, "", authDetails.getAuthorities());
    }

    private String getId(String token) {
        try {
            return getBody(token).getSubject();
        } catch (Exception e) {
            throw InvalidTokenException.EXCEPTION;
        }
    }

    private JwsHeader getHeader(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getHeader();
    }

    private Claims getBody(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody();
    }

}
