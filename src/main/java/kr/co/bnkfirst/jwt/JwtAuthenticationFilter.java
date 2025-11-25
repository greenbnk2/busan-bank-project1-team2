package kr.co.bnkfirst.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import kr.co.bnkfirst.security.MyUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // ⭐ 1) KFTC API 인증 제외
        String path = request.getRequestURI();
        if (path.startsWith("/BNK/api/kftc/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = (String) request.getSession().getAttribute("jwtToken");
//        String token = resolveToken(request);

            if (token != null && jwtProvider.validateToken(token)) {

                // 토큰에서 mid 꺼냄
                String mid = jwtProvider.getMidFromToken(token);

                // DB에서 사용자 조회
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(mid);

                // Authentication 생성
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }


        } catch (Exception ex) {
            logger.error("JWT 필터 오류: " + ex.getMessage());
        }

        // 한 번만 호출하도록 해요 - 손진일
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // /BNK/api/** 는 인증 없이 접근 허용
        return path.startsWith("/BNK/api/");
    }
}
