package moyomoyoe.board.controller;

import moyomoyoe.board.model.dto.CommentDTO;
import moyomoyoe.board.model.dto.KeywordDTO;
import moyomoyoe.board.model.dto.PostDTO;
import moyomoyoe.board.model.dto.RegionDTO;
import moyomoyoe.board.model.service.PostService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

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
    //index.html 연결 Controller
    @GetMapping("/main")
    public String mainPostList(Model model) {
        return "static/index";
    }


    // 날짜별 전체게시글 목록
    @GetMapping("/postlist")
    public String PostList(Model model){
        List<PostDTO> postList = postService.findAllPost();
        model.addAttribute("postList", postList);
        return "board/postlist";
    }

    // index에 키워드 이름 호출
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
    public String getDetailPost(@PathVariable("postId") int postId, Model model){
        // postId에 맞는 게시글 상세 정보를 조회
        PostDTO getDetailPost = postService.findDetailPostById(postId);
        List<CommentDTO> detailPostComment = postService.detailPostComment(postId);

        model.addAttribute("detailPost", getDetailPost);
        model.addAttribute("detailPostComment", detailPostComment);

        // detailpost.html로 데이터 전달 및 렌더링
        return "board/detailpost";  // board/detailpost.html 파일로 이동
    }

    // postId 별 세부 게시글 삭제
    @PostMapping("/detailpost/delete/{postId}")
    public String deletePost(@PathVariable("postId") int postId, RedirectAttributes rAttr) {
        postService.deletePost(postId);
        rAttr.addFlashAttribute("successMessage", "삭제 되었습니다");
        return "redirect:/index";
    }

    // postId 별 세부게시글 댓글등록
    @PostMapping("/detailpost/{postId}")
    public String insertPostComment(@PathVariable("postId") int postId,
                                    @RequestParam("comment") String comment,
                                    RedirectAttributes rAttr){
        CommentDTO commentDTO = new CommentDTO(postId, comment);

        postService.comment(commentDTO);

        rAttr.addFlashAttribute("successMessage", "댓글이 등록되었습니다");
        return "redirect:/board/detailpost/" + postId;
    }

    // 게시글 등록 페이지 이동
    @GetMapping("/createpost")
    public String showCreatePost(Model model){

        model.addAttribute("postDTO", new PostDTO());
        return "board/createpost";
    }

    // 게시글 등록 후 상세페이지로 이동
    @PostMapping("/createpost")
    public String createPost(@ModelAttribute PostDTO postDTO, RedirectAttributes rAttr){

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
    public String updatePost(@PathVariable("postId") int postId, @ModelAttribute PostDTO postDTO, RedirectAttributes rAttr){

        postDTO.setPostId(postId);
        postService.updatePost(postDTO);

        rAttr.addFlashAttribute("successmessage", "게시글이 수정되었습니다.");

        System.out.println("========================================");
        System.out.println("게시글 수정 : " + postDTO);
        System.out.println("========================================");

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
