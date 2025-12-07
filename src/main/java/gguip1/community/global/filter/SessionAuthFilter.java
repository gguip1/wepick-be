package gguip1.community.global.filter;

import gguip1.community.global.context.SecurityContext;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import gguip1.community.global.properties.AuthProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class SessionAuthFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AuthProperties authProperties;
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return authProperties.getExcludedPaths().stream()
                .map(String::trim)
                .anyMatch(pattern -> matcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            HttpSession httpSession = request.getSession(false);

            if (httpSession == null) {
                throw new ErrorException(ErrorCode.UNAUTHORIZED);
            } // 세션이 없으면 인증 실패

            Long userId = (Long) httpSession.getAttribute("userId"); // 세션에서 userId 추출

            if (userId == null) {
                throw new ErrorException(ErrorCode.UNAUTHORIZED);
            } // userId가 없으면 인증 실패

            SecurityContext.setCurrentUserId(userId); // SecurityContext에 userId 설정

            filterChain.doFilter(request, response); // 다음 필터 또는 리소스로 요청 전달
        } catch (ErrorException ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        } finally {
            SecurityContext.clear(); // 요청 처리 후 SecurityContext 정리
        }
    }
}
