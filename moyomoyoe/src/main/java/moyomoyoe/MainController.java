
package moyomoyoe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class MainController {
    @GetMapping("/main")
    public String main(HttpSession session, HttpServletRequest req, Model model) {


        Map<String, Object> userSession = (Map<String, Object>) req.getSession().getAttribute("user");

        model.addAttribute("userSession", userSession);
        System.out.println("[메인]userSession = " + userSession);

        return "/static/main";
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


