package com.example.formapp.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.formapp.constants.CommonConstant;
import com.example.formapp.constants.ErrorConstants;
import com.example.formapp.constants.HeaderConstant;
import com.example.formapp.exception.AuthorizationException;
import com.example.formapp.service.AuthService;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final AuthService authService;
	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, AuthorizationException {
		String header = request.getHeader("Authorization");
		if (StringUtils.isNotBlank(header) && header.startsWith("Bearer ")) {
			String token = header.substring(7);

			String ipAddress = request.getHeader(HeaderConstant.HEADER_X_FORWARDED_FOR);

			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}

			try {
				Map<String, String> datas = new HashMap<>();
				datas.put("token", token);
				datas.put("ipAddress", ipAddress);
				datas.put("lang",
						StringUtils.isNotBlank(request.getHeader(HeaderConstant.HEADER_LANG))
								? request.getHeader(HeaderConstant.HEADER_LANG)
								: CommonConstant.LANG_EN);

				Authentication auth = authService.validateAuthJwt(datas);

				SecurityContextHolder.getContext().setAuthentication(auth);
				filterChain.doFilter(request, response);

			} catch (AuthorizationException ex) {
				log.error("error", ex);
				SecurityContextHolder.clearContext();
				handlerExceptionResolver.resolveException(request, response, null, ex);
			} catch (Exception ex) {
				log.error("error", ex);
				SecurityContextHolder.clearContext();
				handlerExceptionResolver.resolveException(request, response, null, new AuthorizationException(ErrorConstants.ERR_AUTH_JWT_EXPIRED));
			}
		} else {
			handlerExceptionResolver.resolveException(request, response, null, new AuthorizationException(ErrorConstants.ERR_AUTH_JWT_EXPIRED));
		}
	}

}
