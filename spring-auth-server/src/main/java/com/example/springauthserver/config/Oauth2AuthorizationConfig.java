package com.example.springauthserver.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class Oauth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
//	@Autowired
//	private DataSource datasource;
	
//	@Autowired
//	private UserDetailsService userDetailsService;
	
//	private TokenStore tokenStore = new InMemoryTokenStore();
//	private JdbcTokenStore tokenStore = new JdbcTokenStore(datasource);	 // token 存储位置


	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		Map<String, Object> additionalInfo = new HashMap<>();
		additionalInfo.put("hahaha", "hehehe");
		clients.inMemory()
			.withClient("client_1").secret("123456").scopes("ui").authorities("read", "write").authorizedGrantTypes("password", "refersh_token").additionalInformation(additionalInfo)
			.and()
			.withClient("client_2").secret("123456").scopes("ui").authorities("read", "write").authorizedGrantTypes("client_credentials", "refersh_token").additionalInformation(additionalInfo);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(jwtTokenSotre())
				.tokenEnhancer(jwtTokenEnhancer())
				.authenticationManager(authenticationManager)
				.exceptionTranslator(loggingExceptionTranslator());
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()");
	}
	
	@Bean
	public TokenStore jwtTokenSotre() {
		return new JwtTokenStore(jwtTokenEnhancer());
	}
	
	@Bean
	public JwtAccessTokenConverter jwtTokenEnhancer() {
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("test.jks"), "123456".toCharArray());
		CostomJwtAccessTokenConverter converter = new CostomJwtAccessTokenConverter();
		converter.setKeyPair(keyStoreKeyFactory.getKeyPair("test"));
		return converter;
	}
	
	
//	@Bean
//    @Primary
//    public AuthorizationServerTokenServices tokenServices() {
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        // ...
//        tokenServices.setTokenStore(jwtTokenSotre());
//        tokenServices.setTokenEnhancer(jwtTokenEnhancer());
//        return tokenServices;
//    }
	
	@Bean
    public WebResponseExceptionTranslator loggingExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                // This is the line that prints the stack trace to the log. You can customise this to format the trace etc if you like
                e.printStackTrace();

                // Carry on handling the exception
                ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                OAuth2Exception excBody = responseEntity.getBody();
                return new ResponseEntity<>(excBody, headers, responseEntity.getStatusCode());
            }
        };
    }
}
