package moyomoyoe.config;

import moyomoyoe.board.model.dto.PostDTO;
import moyomoyoe.member.auth.model.UserRole;
import moyomoyoe.member.exception.AuthFailHandler;
import moyomoyoe.member.exception.AuthSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private AuthFailHandler authFailHandler;

    @Bean
    public AuthSuccessHandler authSuccessHandler() {
        return new AuthSuccessHandler();
    }

    @Autowired
    public SecurityConfig(AuthFailHandler authFailHandler) {
        this.authFailHandler = authFailHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChainConfigure(HttpSecurity http) throws Exception {

//        http
//                .authorizeRequests()
//                .requestMatchers("/*").permitAll()  // 모든 경로 접근 허용
//                .anyRequest().permitAll()           // 추가적으로 모든 요청에 대한 접근 허용
//                .and();

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/member/user/deleteUser").permitAll();
            auth.requestMatchers("/member/auth/login", "/member/user/signup", "/member/auth/fail", "member/user/region", "member/user/checkAccount", "member/user/findId", "member/user/findPwd", "member/user/changePwd", "/main", "/").permitAll();
            auth.requestMatchers("/member/admin/*").hasAnyAuthority(UserRole.ADMIN.getRole());
            auth.requestMatchers("/member/user/*").hasAnyAuthority(UserRole.USER.getRole(), UserRole.ADMIN.getRole());
            auth.requestMatchers("/member/business/*").hasAnyAuthority(UserRole.BUSINESS.getRole(), UserRole.ADMIN.getRole());
            auth.requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/image/**").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/board/detailpost/delete/**").hasAnyAuthority(UserRole.USER.getRole(), UserRole.ADMIN.getRole());
            // 특정 HTML 파일에 대한 접근을 모두 허용
            auth.requestMatchers("/board/keywordName", "/board/keywordlist", "/board/latestlist", "/board/api/regionlist/cities","/board/api/regionlist/districts", "/board/regionlist", "/board/searchlist", "/board/titlelist").permitAll();
            auth.requestMatchers("/board/detailpost/*").permitAll();

            auth.anyRequest().authenticated();
        }).formLogin(login -> {
            login.loginPage("/member/auth/login");
            login.usernameParameter("account");
            login.passwordParameter("password");
            login.defaultSuccessUrl("/", true);
            login.failureHandler(authFailHandler);
            login.successHandler(authSuccessHandler()).permitAll();
        }).logout(logout -> {
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/member/auth/logout"));
            logout.deleteCookies("JSESSIONID");
            logout.invalidateHttpSession(true);
            logout.logoutSuccessUrl("/");
        }).sessionManagement(session -> {
            session.maximumSessions(1);
            session.invalidSessionUrl("/");
        }).csrf(csrf ->
                csrf.disable()
        );

        return http.build();
    }

}
