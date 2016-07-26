/**
 * 
 */
package com.framework.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;

import com.framework.dao.IGenericHBDao;

/**
 
 */
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	
	private Map<String, Collection<ConfigAttribute>> resourceMap;
	
	private UrlMatcher urlMatcher;
	
	private IGenericHBDao genericHBDao;
	
	public SecurityMetadataSource() {
		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		urlMatcher = new AntUrlPathMatcher();
//		loadResourceDefine();
	}
	
	public void loadResourceDefine() {
		List<Object> args = new ArrayList<Object>();
		StringBuffer buf = new StringBuffer();
		buf.append("select fun.fresource,r.fcode from t_pm_role r join t_pm_roleAuthority ref on r.fid=ref.roleId join ");
		buf.append("t_pm_functionAuthority fun on (fun.fid=ref.authorityId and (fun.ftype=1 or fun.ftype=2))");
//		Set<GrantedAuthority> roles = ContextUtil.getUser().getRoles();
//		if (roles.size() != 0) {
//			buf.append(" where");
//			for (GrantedAuthority role : roles) {
//				buf.append(" r.fcode=? or");
//				args.add(((Role) role).getCode());
//			}
//			buf.setLength(buf.length() - 3);
//		}
		buf.append(" order by fun.fid");
		List<Map<String, Object>> list = genericHBDao.findMapBySQL(buf.toString(), args.toArray());
		for (Map<String, Object> map : list) {
			String roleName = (String) map.get("FCODE");
			String url = (String) map.get("FRESOURCE");
			ConfigAttribute attr = new SecurityConfig(roleName);
			if (resourceMap.containsKey(url)) {
				resourceMap.get(url).add(attr);
			} else {
				Set<ConfigAttribute> attrs = new HashSet<ConfigAttribute>();
				attrs.add(attr);
				resourceMap.put(url, attrs);
			}
		}
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		String url = ((FilterInvocation) object).getRequestUrl();
		Iterator<String> it = resourceMap.keySet().iterator();
		while (it.hasNext()) {
			String resURL = it.next();
			if (urlMatcher.pathMatchesUrl(resURL, url))
				return resourceMap.get(resURL);
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	public void setGenericHBDao(IGenericHBDao genericHBDao) {
		this.genericHBDao = genericHBDao;
	}
	
	public static void main(String[] args) {
		UrlMatcher matcher = new AntUrlPathMatcher();
		boolean b = matcher.pathMatchesUrl("/csnTest.json**", "/csnTest.json?methi=100");
		System.out.println(b);
	}
}
