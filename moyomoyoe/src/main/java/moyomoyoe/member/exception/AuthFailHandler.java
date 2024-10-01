package moyomoyoe.member.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.net.URLEncoder;

@Configuration
public class AuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

    private final View error;

    public AuthFailHandler(View error) {
        this.error = error;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String errorMessage = null;

        if(exception instanceof BadCredentialsException) {

            errorMessage = "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.";

            System.out.println(errorMessage);

        } else if(exception instanceof InternalAuthenticationServiceException) {

            errorMessage = "서버에서 오류가 발생하였습니다.";

            System.out.println(errorMessage);

        } else if(exception instanceof UsernameNotFoundException) {

            errorMessage = "존재하지 않는 아이디 입니다.";

            System.out.println(errorMessage);

        } else if(exception instanceof AuthenticationCredentialsNotFoundException) {

            errorMessage = "인증 요청이 거부 되었습니다.";

            System.out.println(errorMessage);

        } else {

            errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다.";

            System.out.println(errorMessage);

        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");

        setDefaultFailureUrl("/member/auth/fail?message=" + errorMessage);

        super.onAuthenticationFailure(request,response, exception);

    }
}