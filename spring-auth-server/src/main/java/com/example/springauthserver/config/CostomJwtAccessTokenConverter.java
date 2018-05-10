package com.example.springauthserver.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class CostomJwtAccessTokenConverter extends JwtAccessTokenConverter {

	
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
		
		final Map<String, Object> additionalInfo = new HashMap<>();
		additionalInfo.put("customInfo", "some_stuff_here");
		if(authentication.getOAuth2Request().getGrantType().equalsIgnoreCase("password")) {
			User user1 = (User) authentication.getPrincipal();
			User user2 = (User) authentication.getUserAuthentication().getPrincipal();
			additionalInfo.put("user1", user1.getUsername() + " from added");
			additionalInfo.put("user2", user2.getUsername() + " from added");
			enhancedToken.getAdditionalInformation().put("username",
	                user1.getUsername());
			enhancedToken.getAdditionalInformation().put("username",
	                user2.getUsername());
        }
		((DefaultOAuth2AccessToken) enhancedToken).setAdditionalInformation(additionalInfo);
        return enhancedToken;
	}	
}
