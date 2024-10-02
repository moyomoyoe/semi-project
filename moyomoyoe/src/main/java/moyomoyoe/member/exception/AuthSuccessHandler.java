package moyomoyoe.member.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.image.ImageDTO;
import moyomoyoe.member.user.model.dto.RegionDTO;
import moyomoyoe.member.user.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {

        System.out.println("[성공 핸들러] 왓냐?");

        UserDTO user = (UserDTO) authentication.getPrincipal();
        RegionDTO region = userService.getRegionByUserId(user.getId());
        ImageDTO image = userService.getImageById(user.getId());

        System.out.println("[지역은 오니????????] = " + region);
        System.out.println("[이미지 왔니???????] = " + image);

        System.out.println("usernames 값 확인 : " + user.getName());

        Map<String, Object> userSession = new HashMap<>();
        userSession.put("id", user.getId());
        userSession.put("username", user.getName());
        userSession.put("account", user.getAccount());
        userSession.put("nickname", user.getNickname());
        userSession.put("phone", user.getPhone());
        userSession.put("email", user.getEmail());
        if(region != null) {
            userSession.put("region", region.getDistrict());
        }
        if(image != null) {
            userSession.put("image", image.getImageName());
        } else {
            System.out.println("[이미지 널???] = " + image);
        }

        System.out.println("지역은??" + region);

        req.getSession().setAttribute("user",userSession);

        System.out.println("저장 됐냐고" + userSession);

        resp.sendRedirect("/");

    }

}