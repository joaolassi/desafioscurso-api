package com.example.cursoalgaworks.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * *\
 * 
 * @author jmlas Classe criada para a versão mais atual do Spring É necessário
 *         criar um Bean de AuthenticationManagerBuilder e ainda é necessário o
 *         userDetailsServiceBean
 *
 */
@Profile("oauth-security")
@Configuration
@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
public class OAuthServerConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
