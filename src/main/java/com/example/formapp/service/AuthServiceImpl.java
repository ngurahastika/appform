package com.example.formapp.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jboss.logging.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.formapp.config.SecurityConfig;
import com.example.formapp.constants.CacheConstant;
import com.example.formapp.constants.CommonConstant;
import com.example.formapp.constants.ErrorConstants;
import com.example.formapp.constants.LoggerConstant;
import com.example.formapp.constants.MessageConstant;
import com.example.formapp.dto.BaseRes;
import com.example.formapp.dto.JwtDto;
import com.example.formapp.dto.LoginDto;
import com.example.formapp.dto.LoginReq;
import com.example.formapp.dto.UserProfileDto;
import com.example.formapp.enums.JwtType;
import com.example.formapp.enums.UserStatus;
import com.example.formapp.exception.AuthorizationException;
import com.example.formapp.exception.ProcessingException;
import com.example.formapp.model.appform.User;
import com.example.formapp.model.appform.UserSession;
import com.example.formapp.repository.appform.UserRepository;
import com.example.formapp.repository.appform.UserSessionRepository;
import com.example.formapp.utils.CommonUtils;
import com.example.formapp.utils.DateUtils;
import com.example.formapp.utils.HashUtils;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final UserSessionRepository userSessionRepository;
	private final DataUserService dataUserService;
	private final JwtService tokenProvider;
	private final InternalCacheService internalCacheService;
	private final SecurityConfig securityConfig;

	public Authentication validateAuthJwt(Map<String, String> datas) throws JwtException {

		Map<String, Object> claims = tokenProvider.claims(JwtType.LOGIN, datas.get("token"));

		String idUser = String.valueOf(claims.get("AX"));

		String cacheSession = this.internalCacheService.getCache(CacheConstant.SESSION_USER, idUser, String.class);

		if (!CommonUtils.isNotEmpty(cacheSession)) {
			cacheSession = this.userSessionRepository.getSession(Long.valueOf(idUser));

			if (!CommonUtils.isNotEmpty(cacheSession)) {
				throw new AuthorizationException(ErrorConstants.ERR_AUTH_SESSION_IS_EXPIRED);
			}

			this.internalCacheService.saveCache(CacheConstant.SESSION_USER, idUser, cacheSession);
		}

		if (!claims.get("AY").toString().equals(cacheSession)) {
			throw new AuthorizationException(ErrorConstants.ERR_AUTH_SESSION_IS_EXPIRED);
		}

		User user = this.internalCacheService.getCache(CacheConstant.PROFILE_USER, idUser, User.class);

		if (user == null) {

			user = this.userRepository.findById(Long.parseLong(idUser))
					.orElseThrow(() -> new AuthorizationException(ErrorConstants.ERR_AUTH_JWT_EXPIRED));

			this.internalCacheService.saveCache(CacheConstant.PROFILE_USER, idUser, user);
		}

		UserProfileDto dto = new UserProfileDto();

		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		dto.setIpAddress(datas.get("ipAddress"));
		dto.setLang(datas.get("lang"));
		MDC.put(LoggerConstant.ID_USER, user.getId());

		return new UsernamePasswordAuthenticationToken(dto, null, Collections.emptyList());
	}

	public BaseRes login(LoginReq request) {
		BaseRes response = new BaseRes();
		try {

			Optional<User> userDb = this.userRepository.findByEmail(request.getEmail().toLowerCase());

			if (!userDb.isPresent()) {
				throw new AuthorizationException(ErrorConstants.ERR_AUTH_EMAIL_OR_PASS_WRONG);
			}

			User user = userDb.get();

			if (!user.getStatus().equals(UserStatus.ACTIVE)) {
				throw new AuthorizationException(ErrorConstants.ERR_AUTH_EMAIL_OR_PASS_WRONG);
			}

			if (!HashUtils.bcryptVerify(request.getPassword(), user.getPwd())) {

				user.setLoginAttemp(user.getLoginAttemp() + 1);

				if (user.getLoginAttemp() > 2) {
					user.setStatus(UserStatus.LOCKED);
				}
				this.dataUserService.saveAndUpdateUser(user);
				throw new AuthorizationException(ErrorConstants.ERR_AUTH_EMAIL_OR_PASS_WRONG);
			}

			user.setLoginAttemp(0);

			String session = CommonUtils.generateId();

			Optional<UserSession> userSessionDb = this.userSessionRepository.findById(user.getId());

			UserSession userSession = null;
			if (userSessionDb.isPresent()) {
				userSession = userSessionDb.get();
			} else {
				userSession = new UserSession();
				userSession.setId(user.getId());
				userSession.setCreatedBy(CommonConstant.SYSTEM);
				userSession.setCreatedDt(DateUtils.getCurrentSQLTimestamp());
			}

			userSession.setSid(session);
			userSession.setRefreshCount(0);
			userSession.setUpdatedBy(CommonConstant.SYSTEM);
			userSession.setUpdatedDt(DateUtils.getCurrentSQLTimestamp());

			this.dataUserService.saveAndUpdateSessionUserLogin(user, userSession);

			Map<String, Object> claims = new HashMap<>();

			claims.put("AX", user.getId());
			claims.put("AY", session);
			claims.put("ZZ", CommonUtils.generateId());

			JwtDto jwt = this.tokenProvider.generateTokenDto(JwtType.LOGIN, claims, "app-form");

			long exp = jwt.getExpired() - (System.currentTimeMillis() / 1000);
			this.internalCacheService.saveCache(CacheConstant.SESSION_USER, String.valueOf(user.getId()), session);

			LoginDto loginDto = new LoginDto();

			loginDto.setExpired(jwt.getExpired());
			loginDto.setToken(jwt.getToken());
			loginDto.setEmail(user.getEmail());
			loginDto.setName(user.getName());

			response.setUser(loginDto);
			response.setMessage(MessageConstant.LOGIN);
		} catch (AuthorizationException e) {
			log.error("error : ", e);
			throw e;
		} catch (Exception e) {
			log.error("error : ", e);
			throw new ProcessingException();
		}
		return response;
	}

	public BaseRes refreshToken(UserProfileDto uProfile) {
		BaseRes response = new BaseRes();
		try {
			Optional<UserSession> userSessionDb = this.userSessionRepository.findById(uProfile.getId());

			UserSession userSession = userSessionDb.get();

			if (userSession.getRefreshCount() >= securityConfig.getMaxRefreshToken()) {

				this.internalCacheService.evictCache(CacheConstant.PROFILE_USER, String.valueOf(uProfile.getId()));
				throw new AuthorizationException(ErrorConstants.ERR_AUTH_SESSION_IS_EXPIRED);
			}

			userSession.setRefreshCount(userSession.getRefreshCount() + 1);
			userSession.setUpdatedBy(CommonConstant.SYSTEM);
			userSession.setUpdatedDt(DateUtils.getCurrentSQLTimestamp());

			this.dataUserService.updateSessionUserLogin(userSession);

			Map<String, Object> claims = new HashMap<>();

			claims.put("AX", userSession.getId());
			claims.put("AY", userSession.getSid());
			claims.put("ZZ", CommonUtils.generateId());

			JwtDto jwt = this.tokenProvider.generateTokenDto(JwtType.LOGIN, claims, "app-form");

			LoginDto loginDto = new LoginDto();

			loginDto.setExpired(jwt.getExpired());
			loginDto.setToken(jwt.getToken());

			response.setUser(loginDto);
			response.setMessage(MessageConstant.REFRESH_TOKEN);
		} catch (AuthorizationException e) {
			log.error("error : ", e);
			throw e;
		} catch (Exception e) {
			log.error("error : ", e);
			throw new ProcessingException();
		}
		return response;
	}

	public BaseRes logout(UserProfileDto uProfile) {
		Optional<UserSession> userSessionDb = this.userSessionRepository.findById(uProfile.getId());

		UserSession userSession = userSessionDb.get();

		userSession.setSid(null);
		userSession.setRefreshCount(0);
		userSession.setUpdatedBy(CommonConstant.SYSTEM);
		userSession.setUpdatedDt(DateUtils.getCurrentSQLTimestamp());
		this.dataUserService.updateSessionUserLogin(userSession);

		this.internalCacheService.evictCache(CacheConstant.PROFILE_USER, String.valueOf(uProfile.getId()));
		this.internalCacheService.evictCache(CacheConstant.SESSION_USER, String.valueOf(uProfile.getId()));

		BaseRes response = new BaseRes();
		response.setMessage(MessageConstant.LOGOUT);
		return response;
	}
}
