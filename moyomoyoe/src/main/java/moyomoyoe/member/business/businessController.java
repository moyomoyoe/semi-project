package moyomoyoe.member.business;

import jakarta.servlet.http.HttpServletRequest;
import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/member/business")
public class businessController {

    @Autowired
    private UserService userService;

    @GetMapping("/myPage")
    public ModelAndView myPage(ModelAndView mv, Principal principal) {

        String account = principal.getName();

        UserDTO user = userService.getDistrictByAccount(account);

        mv.addObject("user", user);

        mv.setViewName("member/business/myPage");

        return mv;
    }

}
