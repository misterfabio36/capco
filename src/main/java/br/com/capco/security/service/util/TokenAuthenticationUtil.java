package br.com.capco.security.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flywaydb.core.internal.util.DateUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationUtil {
	public	static final long EXPIRATION_TIME = 60000 * 30;
	public	static final String SECRET = "CapcoSecret";
	public static final String TOKEN_PREFIX = "CAPCO_KEY";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "AUTHORITIES_KEY";

	public static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);

		if (token != null) {
			String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();
			
			String authorities = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().get(TokenAuthenticationUtil.AUTHORITIES_KEY).toString();
			Collection authoritiesList =
	                Arrays.stream(authorities.split(","))
	                        .map(SimpleGrantedAuthority::new)
	                        .collect(Collectors.toList());
			
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, authoritiesList);
			}
		}
		return null;
	}

	public static void addAuthentication(HttpServletResponse response, String username, Collection<? extends GrantedAuthority> authorities) {
		List<String> roles = new ArrayList<String>();
		for (GrantedAuthority grantedAuthority : authorities) {
			roles.add(grantedAuthority.getAuthority());
		}
		
		String JWT = Jwts.builder()
				.setSubject(username)
				.claim(AUTHORITIES_KEY, String.join(", ", roles))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

}