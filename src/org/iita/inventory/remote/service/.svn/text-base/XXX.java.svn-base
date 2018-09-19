/**
 * inventory.Struts Aug 13, 2010
 */
package org.iita.inventory.remote.service;

import java.io.IOException;

import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.WSSecurityException;
import org.iita.security.model.User;
import org.iita.security.service.AuthenticationService;
import org.iita.security.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.AuthenticationException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.dao.UserCache;
import org.springframework.security.providers.dao.cache.NullUserCache;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import org.springframework.ws.soap.security.wss4j.callback.SpringDigestPasswordValidationCallbackHandler;

/**
 * @author mobreza
 */
public class XXX extends SpringDigestPasswordValidationCallbackHandler {
	private static Log LOG = LogFactory.getLog(XXX.class);
	private UserCache userCache;
	private UserService userDetailsService;
	private AuthenticationService ldapAuthenticationService = null;

	public XXX() {
		this.userCache = new NullUserCache();
	}

	public void setUserCache(UserCache userCache) {
		super.setUserCache(userCache);
		this.userCache = userCache;
	}

	/**
	 * @param ldapAuthenticationService the ldapAuthenticationService to set
	 */
	public void setLdapAuthenticationService(AuthenticationService ldapAuthenticationService) {
		this.ldapAuthenticationService = ldapAuthenticationService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
		this.userDetailsService = (UserService) userDetailsService;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsService, "userDetailsService is required");
		Assert.notNull(this.ldapAuthenticationService, "ldapAuthenitctionService is required");
	}

	/**
	 * @see org.springframework.ws.soap.security.wss4j.callback.SpringDigestPasswordValidationCallbackHandler#handleUsernameToken(org.apache.ws.security.WSPasswordCallback)
	 */
	@Override
	protected void handleUsernameToken(WSPasswordCallback callback) throws IOException, UnsupportedCallbackException {
		String identifier = callback.getIdentifier();
		UserDetails user = loadUserDetails(identifier);

		LOG.debug("handleUsernameToken");

		if (user != null) {
			callback.setPassword(user.getPassword());

			if (user.getPassword().equals(callback.getPassword())) {
				UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user, callback.getPassword(), user.getAuthorities());

				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Authentication success: " + authRequest.toString());
				}

				SecurityContextHolder.getContext().setAuthentication(authRequest);
			}
		}
	}

	/**
	 * @see org.springframework.ws.soap.security.wss4j.callback.AbstractWsPasswordCallbackHandler#handleUsernameTokenUnknown(org.apache.ws.security.WSPasswordCallback)
	 */
	@Override
	protected void handleUsernameTokenUnknown(WSPasswordCallback callback) throws IOException, UnsupportedCallbackException {
		LOG.debug("handleUsernameTokenUnknown");

		String identifier = callback.getIdentifier();
		try {
			UserDetails userdetails = loadUserDetails(identifier);
			if (userdetails == null)
				throw new WSSecurityException(5);

			UsernamePasswordAuthenticationToken authRequest = null;

			if (userdetails instanceof User) {
				User user = (User) userdetails;
				switch (user.getAuthenticationType()) {
				case PASSWORD:
					LOG.info("User " + user + " set to authenticate with PASSWORD.");

					if (user.getPassword().equals(callback.getPassword()) || this.userDetailsService.isPasswordValid((User) user, callback.getPassword())) {
						authRequest = new UsernamePasswordAuthenticationToken(user, callback.getPassword(), user.getAuthorities());
					} else
						throw new BadCredentialsException("Password hashes don't match");

					break;
				case LDAP:
				default:
					LOG.info("User " + user + " set to authenticate with LDAP.");
					if (ldapAuthenticationService == null) {
						LOG.error("LDAP Auth service not configured!");
						throw new BadCredentialsException("LDAP Auth service not configured.");
					}
					try {
						if (user.getPassword().equals(callback.getPassword()) || this.userDetailsService.isPasswordValid((User) user, callback.getPassword()) || ldapAuthenticationService.authenticate(identifier, callback.getPassword(), user)) {
							LOG.info("User " + user + " authenticated with LDAP method.");
							authRequest = new UsernamePasswordAuthenticationToken(user, callback.getPassword(), user.getAuthorities());
						} else
							throw new BadCredentialsException("LDAP authentication failed for user " + user);
					} catch (Exception e) {
						LOG.error(e, e);
						throw new BadCredentialsException(e.getMessage(), e);
					}

					break;
				}
			} else {
				throw new BadCredentialsException("Something's seriously wrong here!");
			}

			if (authRequest != null) {
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Authentication success: " + authRequest.toString());
				}
				SecurityContextHolder.getContext().setAuthentication(authRequest);
			}
		} catch (AuthenticationException failed) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Authentication request for user '" + identifier + "' failed: " + failed.toString());
			}
			SecurityContextHolder.clearContext();
			throw new WSSecurityException(5);
		}
	}

	private UserDetails loadUserDetails(String username) throws DataAccessException {
		UserDetails user = this.userCache.getUserFromCache(username);

		if (user == null) {
			try {
				user = this.userDetailsService.loadUserByUsername(username);
			} catch (UsernameNotFoundException notFound) {
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Username '" + username + "' not found");
				}
				return null;
			}
			this.userCache.putUserInCache(user);
		}
		return user;
	}
}
