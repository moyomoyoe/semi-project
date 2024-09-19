package moyomoyoe.board.controller;

import moyomoyoe.board.model.dto.PostDTO;
import moyomoyoe.board.model.service.PostService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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














}
