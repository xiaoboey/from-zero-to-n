package com.example.two.authserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServerApplicationTests {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void contextLoads() {
		System.out.println(passwordEncoder.encode("123456"));
		given(passwordEncoder.encode("123456")).willReturn("abc");
	}

}
