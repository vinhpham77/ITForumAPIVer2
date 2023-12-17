package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.*;
import com.caykhe.itforum.models.Authentication;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.repositories.AuthenticationRepository;
import com.caykhe.itforum.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public boolean checkUserByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    public User signUp(SignUpRequest request) {
        if (checkUserByEmail(request.getEmail())) {
            throw new ApiException("Email đã tồn tại", HttpStatus.BAD_REQUEST);
        }
        if (checkUser(request.getUsername())) {
            throw new ApiException("Username đã tồn tại", HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .displayName(request.getDisplayName())
                .role(Role.ROLE_member)
                .birthdate(null)
                .bio(null)
                .gender(null).build();
        return userRepository.save(user);
    }

    public JsonWebToken signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException("Tài khoản hoặc mật khẩu không hợp lệ.", HttpStatus.UNAUTHORIZED));
        //expired in 15 minutes
        var authentication = authenticationRepository.findByUsername(user);
        if (authentication.isPresent()) {
            var refreshToken = authentication.get().getRefreshToken();
            if (!jwtService.isTokenExpired(refreshToken, true)) {
                return JsonWebToken.builder().token(refreshToken).build();
            }
        }

        System.out.println(user);
        var refreshToken = jwtService.generateToken(user, true);
        var newAuthentication = Authentication.builder()
                .username(user)
                .refreshToken(refreshToken)
                .build();

        updateOrCreateIfNot(newAuthentication);
        return JsonWebToken.builder().token(refreshToken).build();
    }
    public Authentication updateOrCreateIfNot(Authentication newAuthentication) {
        Optional<Authentication> authentication = authenticationRepository.findByUsername(newAuthentication.getUsername());
        if (authentication.isEmpty()) {
            return authenticationRepository.save(newAuthentication);
        } else {
            var existingAuthentication = authentication.get();
            existingAuthentication.setRefreshToken(newAuthentication.getRefreshToken());
            return authenticationRepository.save(existingAuthentication);
        }
    }

    boolean checkUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    public JsonWebToken refreshToken(String refreshToken) {
        var recordAuthUser = getAuthenticationAndUser(refreshToken);
        var user = recordAuthUser.user();
        var authentication = recordAuthUser.authentication();

        if(jwtService.isTokenExpired(authentication.getRefreshToken(), true))
            throw new ApiException("Phiên truy cập đã hết hạn. Vui lòng đăng nhập lại!", HttpStatus.PRECONDITION_FAILED);

        var claims = new HashMap<String, Object>();
        claims.put("role", user.getRole());
        claims.put("displayName", user.getDisplayName());

        var accessToken = jwtService.generateToken(claims, user, false);
        return JsonWebToken.builder().token(accessToken).build();
    }

    private RecordAuthUser getAuthenticationAndUser(String refreshToken) {
        String username = jwtService.extractUserName(refreshToken, true);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", HttpStatus.NOT_ACCEPTABLE));
        var authentication = authenticationRepository.findByUsername(user)
                .orElseThrow(() -> new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", HttpStatus.NOT_ACCEPTABLE));
        if (!authentication.getRefreshToken().equals(refreshToken))
            throw new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", HttpStatus.NOT_ACCEPTABLE);

        return RecordAuthUser.builder().user(user).authentication(authentication).build();
    }

    public void logout(String refreshToken) {
        var user = getAuthenticationAndUser(refreshToken).user();
        authenticationRepository.deleteByUsername(user);
    }


}
