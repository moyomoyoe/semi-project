
package moyomoyoe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import moyomoyoe.member.auth.model.UserRole;
import moyomoyoe.member.user.model.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class MainController {
    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping({"/", "/main"})
//    @GetMapping("/main")
//    public ModelAndView main(ModelAndView mv, Principal principal) {
//
//        if(principal != null) {
//
//            String account = principal.getName();
//
//            UserDTO user = userService.getDistrictByAccount(account);
//
//            mv.addObject("user", user);
//        } else {
//            mv.addObject("user", null);
//        }
//
//        mv.setViewName("static/main");
//
//        return mv;
//    }

    @GetMapping({"/","/main"})
    public ModelAndView main(HttpSession session, HttpServletRequest req, Model model, ModelAndView mv) {


        Map<String, Object> userSession = (Map<String, Object>) req.getSession().getAttribute("user");

        model.addAttribute("userSession", userSession);
        System.out.println("[메인]userSession = " + userSession);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                String role = authority.getAuthority();
                if (UserRole.BUSINESS.getRole().equals(role)){
                    System.out.println("사업자 회원인지 확인");

                    mv.setViewName("redirect:/reservation/schedule/storeInfo/"+userSession.get("id"));
                    return mv;
                }
            }
        }
        mv.setViewName("redirect:/board/latestlist");
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

