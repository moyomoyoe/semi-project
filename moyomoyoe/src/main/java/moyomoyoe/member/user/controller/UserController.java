package moyomoyoe.member.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import moyomoyoe.image.ImageDTO;
import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.dto.*;
import moyomoyoe.member.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/member/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public void signup() {}

    @PostMapping("/signup")
    @ResponseBody
    public ModelAndView signup(ModelAndView mv,
                               @ModelAttribute SignupDTO newUserInfo,
                               @RequestParam String phone,
                               @RequestParam String email) {

        String phoneNum = phone.replaceAll(",", "-");
        System.out.println(phoneNum);

        newUserInfo.setPhone(phoneNum);

        String fullEmail = email.replaceAll(",", "@");
        System.out.println(fullEmail);

        newUserInfo.setEmail(fullEmail);

        Integer result = userService.regist(newUserInfo);

        Map<String, String> response = new HashMap<>();

        if(result == null) {
            response.put("status", "fail");
            response.put("message", "이미 가입 된 회원 정보입니다.");
        } else if(result == 0) {
            response.put("status", "fail");
            response.put("message", "회원 가입에 실패했습니다. 다시 시도 해주세요.");
        } else if(result >= 1) {
            response.put("status", "success");
            response.put("message", "회원 가입에 성공 했습니다.");
            System.out.println(response);
            mv.setViewName("redirect:/member/auth/login");
        } else {
            response.put("status", "error");
            response.put("message", "알 수 없는 오류가 발생 했습니다. 다시 시도 해주세요.");
        }
        System.out.println(result);
        return mv;
    }

    @GetMapping(value = "region", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<RegionDTO> findRegionList() {
        System.out.println("JS 내장 함수 fetch");
        List<RegionDTO> region = userService.findAllRegion();
        System.out.println("컨트롤러에서 확인해용 region = " + region);
        return userService.findAllRegion();
    }

    @Autowired
    private DataSource dataSource;

    @GetMapping(value = "checkAccount", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Map<String, Object> checkAccount(@RequestParam String account) {

        System.out.println("아이디 중복 확인 fetch");

        Map<String, Object> resp = new HashMap<>();

        String query = "SELECT COUNT(*) FROM tbl_user WHERE account = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, account);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                resp.put("exists", resultSet.getInt(1) > 0);
//                resp.put("empty", resultSet.getInt(1) == 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.put("error", "오류 발생 쉬먀");
        }

        return resp;
    }

    @GetMapping("/myPage")
    public ModelAndView myPage(ModelAndView mv, Principal principal) {

        String account = principal.getName();

        UserDTO user = userService.getDistrictByAccount(account);

        mv.addObject("user", user);

        mv.setViewName("member/user/myPage");

        return mv;
    }

    @GetMapping("/userInfo")
    public ModelAndView userInfo(ModelAndView mv, Principal principal) {

        String account = principal.getName();

        UserDTO user = userService.getDistrictByAccount(account);

        mv.addObject("user", user);

        mv.setViewName("member/user/userInfo");

        return mv;
    }

    @GetMapping("/editInfo")
    public ModelAndView editInfo(ModelAndView mv, Principal principal) {

        String account = principal.getName();

        UserDTO user = userService.getDistrictByAccount(account);

        mv.addObject("user", user);

        mv.setViewName("member/user/editInfo");

        return mv;
    }

    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("/updateInfo")
    public ModelAndView updateInfo(ModelAndView mv,
                                   @RequestParam(required = false, name = "singleFile") MultipartFile singleFile,
//                                   RedirectAttributes rAttr,
                                   @ModelAttribute ImageDTO newImage,
                                   @ModelAttribute UserDTO newUserInfo,
                                   @RequestParam String phone,
                                   @RequestParam String email,
                                   HttpServletRequest req,
                                   Principal principal) throws IOException {

        System.out.println("회원 수정 되고 있니?");

        String account = principal.getName();
        newUserInfo.setAccount(account);

        UserDTO user = userService.getDistrictByAccount(account);

        String phoneNum = phone.replaceAll(",", "-");
        System.out.println(phoneNum);

        newUserInfo.setPhone(phoneNum);

        String fullEmail = email.replaceAll(",", "@");
        System.out.println(fullEmail);

        newUserInfo.setEmail(fullEmail);

        if(singleFile.isEmpty()) {
            newUserInfo.setImageId(user.getImageId());
        } else {
            System.out.println("[[파일 확인?]] = " + singleFile);

            Resource resource = resourceLoader.getResource("/static/image/");
            System.out.println("경로 확인쓰 = " + resource);

            String filePath = null;

            if (!resource.exists()) {

                //경로 없을 때
                String root = "src/main/resources/static/image/";

                File file = new File(root);
                file.mkdirs();

                filePath = file.getAbsolutePath();
            } else {

                // 경로 있을 때
                filePath = resourceLoader.getResource("/static/image/")
                        .getFile()
                        .getAbsolutePath();
            }

            System.out.println("파일 업로드 경로 확인용~" + filePath);

            String originalFileName = singleFile.getOriginalFilename();
            System.out.println("원본 파일 이름요 = " + originalFileName);

            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            System.out.println("파일 확장자요 = " + extension);

            String savedName = UUID.randomUUID().toString().replace("-", "") + extension;
            System.out.println("저장 될 파일 이름요 = " + savedName);

            boolean isFileSave = false;

            try {
                singleFile.transferTo(new File(filePath + "/" + savedName));

                File savedFile = new File(filePath + "/" + savedName);

                if(savedFile.exists()) {
                    // 파일 저장 완?
                    isFileSave = true;
                    newImage.setImageName("/static/image/" + savedName);
                    newImage.setImageId(newUserInfo.getImageId());
                } else {
                    System.out.println("파일 저장 안 됨!");
                }

                if(isFileSave) {

                    userService.registImage(newImage);

                    newUserInfo.setImageId(newImage.getImageId());

                    System.out.println("[DB에 저장 된 사진 경로?] = " + newImage);

//                rAttr.addFlashAttribute("message", "성공");
//                rAttr.addFlashAttribute("img", "/static/image/" + savedName);

                    System.out.println("업로드 성공!");
                } else {
                    System.out.println("[DB 저장 안 됨!!] : 경로에 파일 없음");
                }

            } catch (IOException e) {
                new File(filePath + "/" + savedName).delete();

//                rAttr.addFlashAttribute("message", "실패");

                System.out.println("업로드 실패!");

                e.printStackTrace();
            }
        }

        Integer result = userService.update(newUserInfo);

        String message = null;

        if(result == null) {
            message = "회원 정보 수정 실패??";
            System.out.println(message);

            mv.setViewName("member/user/editInfo");
        } else if(result == 0) {
            message = "회원 정보 수정에 실패 하였습니다. 다시 시도해주세요.";
            System.out.println(message);

            mv.setViewName("member/user/editInfo");
        } else if(result >= 1) {
            message = "회원 정보 수정 성공하였습니다.";
            System.out.println(message);

            mv.setViewName("redirect:/member/user/userInfo");

            updateSession(req, account);

        } else {
            message = "알 수 없는 오류가 발생하였습니다. 다시 시도 해주세요.";
            System.out.println(message);

            mv.setViewName("member/user/editInfo");
        }

        mv.addObject("user", user);

        return mv;
    }

    public void updateSession(HttpServletRequest req, String account) {

        UserDTO updatedUser = userService.findByAccount(account);

        // 세션 초기화
        req.getSession().removeAttribute("user");

        // 새로운 값들 집어넣기
        Map<String, Object> userSession = new HashMap<>();
        userSession.put("id", updatedUser.getId());
        userSession.put("username", updatedUser.getName());
        userSession.put("account", updatedUser.getAccount());
        userSession.put("nickname", updatedUser.getNickname());
        userSession.put("phone", updatedUser.getPhone());
        userSession.put("email", updatedUser.getEmail());

        RegionDTO region = userService.getRegionByUserId(updatedUser.getId());
        if(region != null) {
            userSession.put("region", region.getDistrict());
        }
        ImageDTO image = userService.getImageById(updatedUser.getId());
        if(image != null) {
            userSession.put("image", image.getImageName());
        }

        // 새로운 값을 다시 세션에 저장
        req.getSession().setAttribute("user", userSession);

        System.out.println("[회원 정보 수정 후] 세션 저장 확인 = " + userSession);

    }

//    @Autowired
//    private ResourceLoader resourceLoader;
//
//    @PostMapping("/updateInfo")
//    public String singleFileUpload(@RequestParam MultipartFile singleFile,
//                                   RedirectAttributes rAttr,
//                                   @ModelAttribute ImageDTO newImage) throws IOException {
//
//        System.out.println("파일 확인? = " + singleFile);
//
//        Resource resource = resourceLoader.getResource("/static/image/");
//        System.out.println("경로 확인쓰 = " + resource);
//
//        String filePath = null;
//
//        if(!resource.exists()) {
//
//            //경로 없을 때
//            String root = "src/main/resources/static/image/";
//
//            File file = new File(root);
//            file.mkdirs();
//
//            filePath = file.getAbsolutePath();
//        } else {
//
//            // 경로 있을 때
//            filePath = resourceLoader.getResource("/static/image/")
//                    .getFile()
//                    .getAbsolutePath();
//        }
//
//        System.out.println("파일 업로드 경로 확인용~" + filePath);
//
//        String originalFileName = singleFile.getOriginalFilename();
//        System.out.println("원본 파일 이름요 = " + originalFileName);
//
//        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
//        System.out.println("파일 확장자요 = " + extension);
//
//        String savedName = UUID.randomUUID().toString().replace("-", "") + extension;
//        System.out.println("저장 될 파일 이름요 = " + savedName);
//
//        try {
//            singleFile.transferTo(new File(filePath + "/" + savedName));
//
//            newImage.setImageName("/static/image/" + savedName);
//
//            userService.registImage(newImage);
//
//            System.out.println("[DB에 저장 된 사진 경로?] = " + newImage);
//
//            rAttr.addFlashAttribute("message", "성공");
//            rAttr.addFlashAttribute("img", "/static/image/" + savedName);
//
//            System.out.println("업로드 성공!");
//
//        } catch(IOException e) {
//            new File(filePath + "/" + savedName).delete();
//
//            rAttr.addFlashAttribute("message", "실패");
//
//            System.out.println("업로드 실패!");
//
//            e.printStackTrace();
//        }
//
//        return "redirect:/member/user/editInfo";
//    }

    @GetMapping("/findId")
    public void findId() {
        System.out.println("[아이디 찾기] 왓니?");
    }

    @PostMapping("/findId")
    @ResponseBody
    public ModelAndView findId(ModelAndView mv,
                               @RequestParam String username,
                               @RequestParam String email){

        FindIdDTO findAccount = userService.findAccount(username, email);

        System.out.println(username + email);

        if(findAccount != null) {
            mv.addObject("account", findAccount.getAccount());
            mv.addObject("message", "아이디는  " + findAccount.getAccount() + "  입니다.");
            mv.addObject("redirect", true); // 로그인 페이지로
        } else {
            mv.addObject("message", "일치하는 정보가 없습니다.");
            mv.addObject("redirect", false); // 로그인 페이지로 안 감
        }
        System.out.println("[아이디 잘 가져오니?] findAccount = " + findAccount);
        mv.setViewName("member/user/findId");
        return mv;
    }


    @GetMapping("/findPwd")
    public void findPwd() {
        System.out.println("[비밀번호 찾기] 왓니?");
    }

    @PostMapping("/findPwd")
    @ResponseBody
    public ModelAndView findPwd(ModelAndView mv,
                                @RequestParam String account,
                                @RequestParam String email) {

        System.out.println("[비밀번호 찾기 이메일 확인] 왓니?");

        FindPwdDTO findPwd = userService.findPwd(account, email);

        if(findPwd != null) {
            mv.addObject("message", "이메일 확인 완료 되었습니다. 비밀번호를 변경해주세요.");
            mv.addObject("account", account);
            mv.addObject("redirect", true);
        } else {
            mv.addObject("message", "일치하는 정보가 없습니다.");
            mv.addObject("redirect", false);
        }

        mv.setViewName("/member/user/findPwd");
        return mv;
    }

    @PostMapping("/changePwd")
    @ResponseBody
    public ModelAndView changePwd(ModelAndView mv,
                                  @ModelAttribute FindPwdDTO newPwd) {

        System.out.println("[비밀번호 초기화] 왔니? ");

        Integer result = userService.updatePwd(newPwd);
        String message = null;

        if(result == null) {
            message = "비밀번호 초기화 실패??";
            mv.addObject("done", message);
            System.out.println(message);

            mv.setViewName("member/user/findPwd");
        } else if(result == 0) {
            message = "비밀번호 초기화에 실패 하였습니다. 다시 시도해주세요.";
            mv.addObject("done", message);

            System.out.println(message);

            mv.setViewName("member/user/findPwd");
        } else if(result >= 1) {
            message = "비밀번호 초기화 성공하였습니다.";
            mv.addObject("done", message);

            System.out.println(message);
            mv.setViewName("redirect:/member/auth/login");

        } else {
            message = "알 수 없는 오류가 발생하였습니다. 다시 시도 해주세요.";
            mv.addObject("done", message);

            System.out.println(message);

            mv.setViewName("member/user/findPwd");
        }


        mv.setViewName("/member/auth/login");
        return mv;
    }

    @PostMapping("/deleteUser")
    public String deleteUser(HttpSession session,
                             RedirectAttributes rAttr,
                             Principal principal) {

        System.out.println("[회원 탈퇴] 왓니?");

        String account = principal.getName();

        System.out.println("account = " + account);

        if(account != null) {

            userService.deleteUser(account);

            rAttr.addFlashAttribute("message", "회원 탈퇴가 완료 되었습니다.");
            System.out.println("[회원 탈퇴] 성공~!~!");

            // 세션 무효화
            session.invalidate();

            return "redirect:/main";
        } else {

            System.out.println("[회원 탈퇴] 실패~~~!");
            rAttr.addFlashAttribute("message", "사용자 정보가 존재하지 않습니다. 다시 시도해주세요.");

            return "redirect:/member/user/userInfo";
        }
    }


}