package service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JWTTokenGenerator {

	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static String generateToken(String username) {
		Date now = new Date();
		Date expiration = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));

		return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(expiration).signWith(SECRET_KEY)
				.compact();
	}
}
