package com.framework.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;
import org.springframework.util.Assert;

import com.framework.core.ContextUtil;
 

/**
 
 */
public class AccessValueVoter implements AccessDecisionVoter {

	private AuthenticationTrustResolver authenticationTrustResolver;
	
	private UrlMatcher urlMatcher;
	
	public AccessValueVoter() {
		authenticationTrustResolver = new AuthenticationTrustResolverImpl();
		urlMatcher = new AntUrlPathMatcher();
	}

	private boolean isFullyAuthenticated(Authentication authentication) {
		return (!authenticationTrustResolver.isAnonymous(authentication) && !authenticationTrustResolver.isRememberMe(authentication));
	}

	public void setAuthenticationTrustResolver(AuthenticationTrustResolver authenticationTrustResolver) {
		Assert.notNull(authenticationTrustResolver, "AuthenticationTrustResolver cannot be set to null");
		this.authenticationTrustResolver = authenticationTrustResolver;
	}

	public boolean supports(ConfigAttribute attribute) {
		if ((attribute.getAttribute() != null)
				&& (AuthenticatedVoter.IS_AUTHENTICATED_FULLY.equals(attribute.getAttribute()) || AuthenticatedVoter.IS_AUTHENTICATED_REMEMBERED.equals(attribute.getAttribute()) || AuthenticatedVoter.IS_AUTHENTICATED_ANONYMOUSLY.equals(attribute.getAttribute()))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		int result = ACCESS_ABSTAIN;
		for (ConfigAttribute attribute : attributes) {
			if (this.supports(attribute)) {
				result = ACCESS_DENIED;
				if (AuthenticatedVoter.IS_AUTHENTICATED_FULLY.equals(attribute.getAttribute())) {
					if (isFullyAuthenticated(authentication) && checkAccess(authentication, object)) {
						return ACCESS_GRANTED;
					}
				}

				if (AuthenticatedVoter.IS_AUTHENTICATED_REMEMBERED.equals(attribute.getAttribute())) {
					if (authenticationTrustResolver.isRememberMe(authentication) || isFullyAuthenticated(authentication)) {
						return ACCESS_GRANTED;
					}
				}

				if (AuthenticatedVoter.IS_AUTHENTICATED_ANONYMOUSLY.equals(attribute.getAttribute())) {
					if (authenticationTrustResolver.isAnonymous(authentication) || isFullyAuthenticated(authentication) || authenticationTrustResolver.isRememberMe(authentication)) {
						return ACCESS_GRANTED;
					}
				}
			}
		}
		return result;
	}
	
	private boolean checkAccess(Authentication authentication, Object object) {
		if (authentication.getName().equalsIgnoreCase("admin"))
			return true;
		String url = ((FilterInvocation) object).getRequestUrl();
		if (urlMatcher.pathMatchesUrl("/login.do", url) || urlMatcher.pathMatchesUrl("/logout.do", url))
			return true;
		if (url.indexOf("/dwr") >= 0)
			return true;
		int pos = url.lastIndexOf("/");
		String path = url.substring(0, pos);
		Collection<GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			/*		Menu menu = (Menu) authority;
			if (menu.getUrl() != null  && path.endsWith(menu.getUrl())) {
				int rights = menu.getRights();
				String action = url.substring(pos + 1);
				if (action.startsWith("forward")) {
					return rights > 0;
				} 
				
				else if (action.startsWith("find")) {
					return ContextUtil.checkAccess(rights, 1);
				} else if (action.startsWith("add")) {
					return ContextUtil.checkAccess(rights, 2) || ContextUtil.checkAccess(rights, 4);
				} else if (action.startsWith("update")) {
					return ContextUtil.checkAccess(rights, 3) || ContextUtil.checkAccess(rights, 4);
				} else if (action.startsWith("save")) {
					return ContextUtil.checkAccess(rights, 4);
				} else if (action.startsWith("copy")) {
					return ContextUtil.checkAccess(rights, 5);
				} else if (action.startsWith("delete")) {
					return ContextUtil.checkAccess(rights, 6);
				} else if (action.startsWith("audit")) {
					return ContextUtil.checkAccess(rights, 7);
				} else if (action.startsWith("import")) {
					return ContextUtil.checkAccess(rights, 8);
				} else if (action.startsWith("export")) {
					return ContextUtil.checkAccess(rights, 9);
				} else if (action.startsWith("upload")) {
					return ContextUtil.checkAccess(rights, 10);
				} else if (action.startsWith("download")) {
					return ContextUtil.checkAccess(rights, 11);
				}
				
				break;
			
			}	*/
		}
		return false;
	}
}