
package moyomoyoe;

import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @RequestMapping({"/", "/main"})
    @GetMapping("/main")
    public ModelAndView main(ModelAndView mv, Principal principal) {

        String account = principal.getName();

        UserDTO user = userService.getDistrictByAccount(account);

        mv.addObject("user", user);

        mv.setViewName("static/main");

        return mv;
    }

    @GetMapping("/admin/page")
    public ModelAndView adminPage(ModelAndView mv) {
        mv.setViewName("admin/admin");
        return mv;
    }
    @GetMapping("/user/page")
    public ModelAndView userPage(ModelAndView mv) {
        mv.setViewName("user/user");
        return mv;
    }
}

