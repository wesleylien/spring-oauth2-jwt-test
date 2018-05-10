package com.example.springauthserver.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	
//	@Autowired
//    private AuthorizationServerTokenServices tokenServices;

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public Principal getUser(Principal principal) {
		return principal;
	}
	
//	@RequestMapping(value = "/getSomething", method = RequestMethod.GET)
//    public String getSection(OAuth2Authentication authentication) {
//        Map<String, Object> additionalInfo = tokenServices.getAccessToken(authentication).getAdditionalInformation();
//
//        String customInfo = (String) additionalInfo.get("customInfo");
//        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) additionalInfo.get("authorities");
//
//        // Play with authorities
//
//        return customInfo;
//    }
}
