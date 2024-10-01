package moyomoyoe.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import moyomoyoe.board.model.dto.CommentDTO;
import moyomoyoe.board.model.dto.KeywordDTO;
import moyomoyoe.board.model.dto.PostDTO;
import moyomoyoe.board.model.dto.RegionDTO;
import moyomoyoe.board.model.service.PostService;
import moyomoyoe.member.auth.model.dto.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class PostController {

    private static final Logger logger = LogManager.getLogger(PostController.class);
    private final PostService postService;
    private final MessageSource messageSource;

    @Autowired
    public PostController(PostService postService, MessageSource messageSource) {
        this.postService = postService;
        this.messageSource = messageSource;
    }

    //searchlist.html 연결 Controller
    @GetMapping("/searchlist")
    public String getSearchList(Model model) {
        return "board/searchlist";
    }

    // 날짜별 전체게시글 목록
    @GetMapping("/latestlist")
    public String Latestlist(Model model){
        List<PostDTO> latestlist = postService.findAllPost();
        model.addAttribute("latestList", latestlist);
        return "board/latestlist";
    }

    // searchlist에 키워드 이름 호출
    @GetMapping(value = "/keywordName", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<KeywordDTO>findKeywordName(){
        System.out.println("JavaScript 내장 함수인 fetch");

        List<KeywordDTO> keywordNameList = postService.findKeywordName();

        return keywordNameList;
    }

    // 키워드별 게시글 목록
    @GetMapping("/keywordlist")
    public String keywordList(@RequestParam("keywordId") int keywordId, Model model){

        List<PostDTO> keywordList = postService.findKeywordList(keywordId);

        model.addAttribute("keywordList", keywordList);

        return "board/keywordlist";
    }

    // 시 목록을 JSON으로 반환하는 API
    @GetMapping("/api/regionlist/cities")
    @ResponseBody
    public List<RegionDTO> getCityList() {
        return postService.findRegionCityList();
    }

    // 선택한 시에 해당하는 구 목록을 JSON으로 반환하는 API
    @GetMapping("/api/regionlist/districts")
    @ResponseBody
    public List<RegionDTO> getDistrictList(@RequestParam String city) {
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("City parameter is required");
        }
        return postService.findRegionDistrictList(city);
    }

    // 불러온 시/구 에 맞는 지역 별 게시글 목록
    @GetMapping("/regionlist")
    public String getPostsByRegion(@RequestParam String city, @RequestParam int regionCode, Model model) {
        // 시와 구에 따른 게시글 목록을 조회
        List<PostDTO> regionList = postService.findPostsByRegion(city, regionCode);

        // 조회된 게시글 목록을 모델에 추가
        model.addAttribute("regionList", regionList);
        model.addAttribute("city", city);
        model.addAttribute("regionCode", regionCode);

        // regionlist.html로 데이터 전달 및 렌더링
        return "board/regionlist";
    }

    // 제목검색 게시글 목록
    @GetMapping("/titlelist")
    public String titleList(@RequestParam("title") String title, Model model){
        List<PostDTO> titleList = postService.findTitleList(title);

        System.out.println("조회된 titleList 데임이터" + titleList);

        model.addAttribute("titleList", titleList);

        return "board/titlelist";
    }

    // postId 별 세부 게시글 내용, 댓글
    @GetMapping("/detailpost/{postId}")
    public String getDetailPost(@PathVariable("postId") int postId,
                                Model model,
                                RedirectAttributes rAttr){

        // 현재 로그인한 사용자 정보 가져오기 (SecurityContextHolder 사용)
        Authentication authPost = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authPost = " + authPost);

        UserDTO userDTO = (UserDTO) authPost.getPrincipal();
        int loggedInUserId = userDTO.getId();
        String loggedInNickname = userDTO.getNickname();

        // postId에 맞는 게시글 상세 정보를 조회
        PostDTO getDetailPost = postService.findDetailPostById(postId);
        List<CommentDTO> detailPostComment = postService.detailPostComment(postId);

        System.out.println("authPost = " + authPost);

        if (authPost ==  null&& !getDetailPost.getUserOpen()){

            System.out.println("로그인 후 접근 가능합니다.");

            model.addAttribute("userOpenError", "로그인 후 접근 가능합니다.");

            return "redirect:/board/mainSearchList";
        }

        // 게시글 작성자와 로그인한 사용자 정보 전달
        int postOwnerId = getDetailPost.getUserId();  // 게시글 작성자의 ID

        model.addAttribute("loggedInUserId", loggedInUserId);
        model.addAttribute("loggedInNickname", loggedInNickname);
        model.addAttribute("detailPost", getDetailPost);
        model.addAttribute("detailPostComment", detailPostComment);
        model.addAttribute("postOwnerId", postOwnerId);  // 게시글 작성자 ID 전달

        // detailpost.html로 데이터 전달 및 렌더링
        return "board/detailpost";  // board/detailpost.html 파일로 이동
    }

    // postId 별 세부 게시글 삭제
    @PostMapping("/detailpost/delete/{postId}")
    public String deletePost(@PathVariable("postId") int postId, RedirectAttributes rAttr) {

        // 현재 로그인한 사용자 정보 가져오기 (SecurityContextHolder 사용)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO) auth.getPrincipal();
        int loggedInUserId = userDTO.getId();

        // postId에 해당하는 게시글을 조회하여 작성자 확인
        PostDTO post = postService.findDetailPostById(postId);

        boolean owner = loggedInUserId == post.getUserId();

        // 현재 사용자의 권한 확인 (ADMIN 권한이 있는지)
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        // 게시글 작성자 또는 관리자인지 확인
        if (owner || isAdmin) {
            // 삭제 권한이 있는 경우 게시글 삭제
            postService.deletePost(postId);  // 여기서 오류가 발생하는지 확인 필요
        }

        // 삭제 성공 메시지
        rAttr.addFlashAttribute("successMessage", "게시글이 삭제되었습니다.");
        return "redirect:/board/searchlist";  // 삭제 후 게시글 목록으로 이동

    }

    // postId 별 세부게시글 댓글등록
    @PostMapping("/detailpost/{postId}")
    public String insertPostComment(@PathVariable("postId") int postId,
                                    @RequestParam("comment") String comment,
                                    HttpSession session,
                                    RedirectAttributes rAttr){
        // 세션에서 사용자 정보 가져오기
        Map<String, Object> userSession = (Map<String, Object>) session.getAttribute("user");

        // 사용자가 로그인되지 않았다면 처리 (로그인하지 않은 사용자 접근 방지)
        if (userSession == null) {
            rAttr.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/auth/login";
        }

        Integer userId = (Integer) userSession.get("id");
        String nickname = (String) userSession.get("nickname");

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPostId(postId);
        commentDTO.setComment(comment);
        commentDTO.setNickname(nickname);
        commentDTO.setUserId(userId);

        postService.comment(commentDTO);

        rAttr.addFlashAttribute("successMessage", "댓글이 등록되었습니다");
        return "redirect:/board/detailpost/" + postId;
    }

    // 게시글 등록 페이지 이동
    @GetMapping("/createpost")
    public String showCreatePost(Model model, Principal principal, RedirectAttributes rAttr){

        if (principal == null) {
            rAttr.addFlashAttribute("errormessage", "로그인 후 게시글을 작성할 수 있습니다.");
            return "redirect:/member/auth/login";
        }

        model.addAttribute("postDTO", new PostDTO());
        return "board/createpost";
    }

    // 게시글 등록 후 상세페이지로 이동
    @PostMapping("/createpost")
    public String createPost(@ModelAttribute PostDTO postDTO, RedirectAttributes rAttr){


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        String nickname = userDTO.getNickname();
        int userId = userDTO.getId();

        postDTO.setUserId(userId);
        postDTO.setNickname(nickname);

        int postId = postService.createPost(postDTO);

        rAttr.addFlashAttribute("successmessage", "게시글이 등록되었습니다.");

        System.out.println("========================================");
        System.out.println("게시글 등록 : " + postDTO);
        System.out.println("========================================");

        return "redirect:/board/detailpost/" + postId;
    }

    // 게시글 수정 페이지 이동
    @GetMapping("/editpost/{postId}")
    public String editPostForm(@PathVariable("postId") int postId, Model model){

        PostDTO postDTO = postService.findDetailPostById(postId);
        model.addAttribute("postDTO", postDTO);

        System.out.println("========================================");
        System.out.println("게시글 수정으로 : " + postDTO);
        System.out.println("========================================");

        return "/board/editpost";
    }

    // 게시글 수정 후 상세페이지로 이동
    @PostMapping("/editpost/{postId}")
    public String updatePost(@PathVariable("postId") int postId, @ModelAttribute PostDTO postDTO, Authentication authentication, RedirectAttributes rAttr){

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        postDTO.setUserId(userDTO.getId());
        postDTO.setNickname(userDTO.getNickname());

        postDTO.setPostId(postId);
        postService.updatePost(postDTO);

        rAttr.addFlashAttribute("successmessage", "게시글이 수정되었습니다.");

        return "redirect:/board/detailpost/" + postId;
    }

    // 게시글 댓글 삭제
    @PostMapping("/detailpost/delete/comment/{commentId}")
    public String deleteComment(@PathVariable("commentId") int commentId, @RequestParam("postId") int postId, RedirectAttributes rAttr) {

        postService.deleteComment(commentId);
        rAttr.addFlashAttribute("successMessage", "댓글이 삭제되었습니다.");

        return "redirect:/board/detailpost/" + postId;
    }

}
