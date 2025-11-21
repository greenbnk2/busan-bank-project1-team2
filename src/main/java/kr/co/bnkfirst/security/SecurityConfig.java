package kr.co.bnkfirst.security;

import jakarta.servlet.http.HttpSession;
import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /*
        날짜 : 2025/11/20
        이름 : 이준우
        내용 : security 관련 내용 추가
     */

    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UsersService usersService) throws Exception {

        // 로그인 설정
        http.formLogin(form -> form
                .loginPage("/member/main")
                .loginProcessingUrl("/member/login")
                .usernameParameter("mid")
                .passwordParameter("mpw")

                // successHandler 하나만
                .successHandler((request, response, authentication) -> {
                    String mid = authentication.getName();
                    UsersDTO user = usersService.findByMid(mid);

                    HttpSession session = request.getSession();
                    session.setAttribute("loginUser", user);
                    session.setAttribute("sessionStart", System.currentTimeMillis());
                    session.setMaxInactiveInterval(1200);  // 20분

                    // context-path
                    response.sendRedirect(request.getContextPath() + "/main/main");
                })
                .permitAll()
        );

        // 로그아웃
        http.logout(logout -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    System.out.println(">>>> LOGOUT SUCCESS: 사용자 로그아웃 완료!");
                    if (authentication != null) {
                        System.out.println(">>>> 로그아웃한 사용자: " + authentication.getName());
                    }
                    response.sendRedirect("/member/main?logout");
                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        // 인가 규칙
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/css/**", "/js/**", "/images/**",
                        "/components/**",
                        "/upload/**",
                        "/member/**",
                        "/main/**",
                        "/api/**",
                        "/member/api/**",
                        "/cs/**",
                        "/info/**",
                        "/board/**",
                        "/kiwoom/**",
                        "/docs/**",
                        "/product/**",
                        "/qna/**",
                        "/stock/**",
                        "/tologo/**",
                        "/mypage/**",
                        "/ws/**",
                        "/retirement-renew/**"
                ).permitAll()

                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/mypage/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
        );

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
