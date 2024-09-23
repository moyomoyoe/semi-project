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

    // 시와 구 목록을 JSON으로 반환하는 메서드 (API)
    @GetMapping("/regionlistdata")
    @ResponseBody  // 이 부분은 API에서 JSON을 반환하기 때문에 반드시 필요합니다.
    public List<RegionDTO> getRegionData(@RequestParam(required = false) String city) {
        if (city == null || city.isEmpty()) {
            return postService.findRegionCityList();
        } else {
            return postService.findRegionDistrictList(city);
        }
    }

    // 페이지를 렌더링하는 메서드
    @GetMapping("/regionlist")
    public String districtList(@RequestParam(required = false) String city, Model model) {
        if (city != null && !city.isEmpty()) {
            List<RegionDTO> districtList = postService.findRegionDistrictList(city);
            model.addAttribute("districtList", districtList);
            model.addAttribute("city", city);  // 선택된 시도 모델에 추가
        }
        // 템플릿 페이지로 이동 (regionlist.html로 이동)
        return "board/regionlist";
    }



        @GetMapping("/titlelist")
    public String titleList(@RequestParam("title") String title, Model model){
        List<PostDTO> titleList = postService.findTitleList(title);

        System.out.println("조회된 titleList 데임이터" + titleList);

        model.addAttribute("titleList", titleList);

        return "board/titlelist";
    }















}
