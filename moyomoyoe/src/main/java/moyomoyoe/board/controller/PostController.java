package moyomoyoe.board.controller;

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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/board")
public class PostController {

    /* 게시판 전체 조회
    *  게시판 지역 별 조회
    *  게시판 키워드 별 조회
    *  게시판 제목 검색 별 조회 */

    private static final Logger logger = LogManager.getLogger(PostController.class);

    private final PostService postService;
    private final MessageSource messageSource;

    @Autowired
    public PostController(PostService postService, MessageSource messageSource) {
        this.postService = postService;
        this.messageSource = messageSource;
    }

    @GetMapping("/postlist")
    public String PostList(Model model){

        List<PostDTO> postList = postService.findAllPost();

        model.addAttribute("postList", postList);

        return "board/postlist";
    }

    @GetMapping("/keywordlist")
    public String KeywordList(@RequestParam("keywordId") int keywordId, Model model){

        List<PostDTO> keywordList = postService.findKeywordList(keywordId);

        model.addAttribute("keywordId", keywordId);
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

        @GetMapping("/titlelist")
    public String titleList(@RequestParam("title") String title, Model model){
        List<PostDTO> titleList = postService.findTitleList(title);

        System.out.println("조회된 titleList 데임이터" + titleList);

        model.addAttribute("titleList", titleList);

        return "board/titlelist";
    }

    @GetMapping("/detailpost/{postId}")
    public String detailPost(@PathVariable("postId") int postId, Model model){
        // postId에 맞는 게시글 상세 정보를 조회
        PostDTO detailPost = postService.findDetailPostById(postId);

        System.out.println("detailPost = " + detailPost);

        if (detailPost == null) {
            // postId에 맞는 게시글이 없으면 에러 페이지로 이동
            return "error/404";
        }

        // 조회된 게시글 상세 정보를 모델에 추가
        model.addAttribute("detailPost", detailPost);

        // detailpost.html로 데이터 전달 및 렌더링
        return "board/detailpost";  // board/detailpost.html 파일로 이동
    }

    @PostMapping("/detailpost/delete/{postId}")
    public String deletePost(@PathVariable("postId") int postId, RedirectAttributes rAttr) {
        postService.deletePost(postId);
        rAttr.addFlashAttribute("successMessage", "삭제 되었습니다");
        return "redirect:/index.html";
    }














}
