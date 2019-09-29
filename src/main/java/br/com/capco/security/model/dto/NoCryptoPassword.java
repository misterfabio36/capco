package br.com.capco.security.model.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

public class NoCryptoPassword implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(rawPassword);
	}

}
