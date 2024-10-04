package moyomoyoe.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import moyomoyoe.board.model.dto.*;
import moyomoyoe.board.model.service.PostService;
import moyomoyoe.image.ImageDTO;
import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/board")
public class PostController {

    private static final Logger logger = LogManager.getLogger(PostController.class);
    private final PostService postService;
    private final MessageSource messageSource;
    private final ImageController imageController;
    @Autowired
    private UserService userService;



    @Autowired
    public PostController(PostService postService, MessageSource messageSource, ImageController imageController, UserService userService) {
        this.postService = postService;
        this.messageSource = messageSource;
        this.imageController = imageController;
        this.userService = userService;
    }

    //searchlist.html 연결 Controller
    @GetMapping("/searchlist")
    public String searchList(Model model) {
        List<PostDTO> latestList = postService.findAllPost();
        model.addAttribute("latestList", latestList);
        return "board/searchlist";
    }

    // 날짜별 전체게시글 목록
    @GetMapping("/latestlist")
    public String latestList(Model model){
        List<PostDTO> latestList = postService.findAllPost();
        System.out.println("latestList = " + latestList);
        model.addAttribute("latestList", latestList);
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

        // 현재 로그인한 사용자 정보 가져오기
        Authentication authPost = SecurityContextHolder.getContext().getAuthentication();

        // 비회원일 경우 처리
        if (authPost == null || !authPost.isAuthenticated() || authPost.getPrincipal().equals("anonymousUser")) {
            // postId에 맞는 게시글 상세 정보를 조회
            PostDTO getDetailPost = postService.findDetailPostById(postId);

            // 비회원이지만 게시글이 비회원 열람 가능 (userOpen이 true)인 경우 접근 허용
            if (!getDetailPost.getUserOpen()) {
                // userOpen이 false인 경우, 로그인 요구
                System.out.println("로그인 후 접근 가능합니다.");
                rAttr.addFlashAttribute("userOpenError", "로그인 후 접근 가능합니다.");
                return "redirect:/board/searchList";
            }

            // postId에 맞는 댓글 조회
            List<CommentDTO> detailPostComment = postService.detailPostComment(postId);

            // 프로필 이미지 경로 가져오기
//            int imageId = postService.getImageByUserId(getDetailPost.getUserId());
//            System.out.println("imageId = " + imageId);
//
//            String profileImage = postService.getProfileImageById(imageId);
//            System.out.println("profileImage = " + profileImage);
            int imageId = postService.getImageByUserId(getDetailPost.getUserId());
            ImageDTO profileImage = postService.getProfileImageById(imageId);

            // 이미지 처리
            ImageDTO imageDTO = postService.getImageById(getDetailPost.getImageId());
            if (imageDTO == null || imageDTO.getImageName() == null) {
                imageDTO = new ImageDTO();
                imageDTO.setImageName("/static/image/image1.png"); // 기본 이미지 경로 설정
            }

            // 비회원도 볼 수 있는 게시글 데이터 전달
            model.addAttribute("detailPost", getDetailPost);
            model.addAttribute("detailPostComment", detailPostComment);
            model.addAttribute("imageDTO", imageDTO);
            model.addAttribute("profileImage", profileImage);

            return "board/detailpost";
        }

        // 로그인된 사용자일 경우 처리
        UserDTO userDTO;
        try {
            userDTO = (UserDTO) authPost.getPrincipal();  // UserDTO 캐스팅
        } catch (ClassCastException e) {
            // 캐스팅이 실패한 경우 예외 처리
            System.out.println("로그인한 사용자 정보 가져오기 실패.");
            rAttr.addFlashAttribute("authError", "로그인한 사용자 정보를 가져올 수 없습니다.");
            return "redirect:/board/searchList";
        }

        // 로그인한 사용자 정보
        int loggedInUserId = userDTO.getId();
        String loggedInNickname = userDTO.getNickname();

        // postId에 맞는 게시글 상세 정보를 조회
        PostDTO getDetailPost = postService.findDetailPostById(postId);
        List<CommentDTO> detailPostComment = postService.detailPostComment(postId);

        // 프로필 이미지 경로 가져오기
//            int imageId = postService.getImageByUserId(getDetailPost.getUserId());
//            System.out.println("imageId = " + imageId);
//
//            String profileImage = postService.getProfileImageById(imageId);
//            System.out.println("profileImage = " + profileImage);
        int imageId = postService.getImageByUserId(getDetailPost.getUserId());
        ImageDTO profileImage = postService.getProfileImageById(imageId);

        // 이미지 처리
        ImageDTO imageDTO = postService.getImageById(getDetailPost.getImageId());
        if (imageDTO == null || imageDTO.getImageName() == null) {
            imageDTO = new ImageDTO();
            imageDTO.setImageName("/static/image/image1.png"); // 기본 이미지 경로 설정
        }

        // 게시글 작성자 ID 가져오기
        int postOwnerId = getDetailPost.getUserId();

        // 모델에 데이터 추가
        model.addAttribute("loggedInUserId", loggedInUserId);
        model.addAttribute("loggedInNickname", loggedInNickname);
        model.addAttribute("detailPost", getDetailPost);
        model.addAttribute("detailPostComment", detailPostComment);
        model.addAttribute("imageDTO", imageDTO);
        model.addAttribute("profileImage", profileImage);
        model.addAttribute("postOwnerId", postOwnerId);

        // detailpost.html로 데이터 전달 및 렌더링
        return "board/detailpost";
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

    @Autowired
    private ResourceLoader resourceLoader;

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
    public String createPost(@ModelAttribute PostDTO postDTO,
                             RedirectAttributes rAttr,
                             @RequestParam(required = false, name = "singleFile") MultipartFile singleFile,
                             @ModelAttribute ImageDTO newImage,
                             HttpServletRequest req) throws IOException {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        String nickname = userDTO.getNickname();
        int userId = userDTO.getId();


        postDTO.setUserId(userId);
        postDTO.setNickname(nickname);


        if(singleFile == null || singleFile.isEmpty()) {
//            postDTO.setImageId(postDTO.getImageId());
            postDTO.setImageId(1);
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
                    newImage.setImageId(postDTO.getImageId());
                } else {
                    System.out.println("파일 저장 안 됨!");
                }

                if(isFileSave) {

                    postService.registImage(newImage);

                    postDTO.setImageId(newImage.getImageId());

                    System.out.println("[DB에 저장 된 사진 경로?] = " + newImage);
                    System.out.println("업로드 성공!");

                } else {
                    System.out.println("[DB 저장 안 됨!!] : 경로에 파일 없음");
                }

            } catch (IOException e) {
                new File(filePath + "/" + savedName).delete();
                System.out.println("업로드 실패!");

                e.printStackTrace();

            }
        }

        int postId = postService.createPost(postDTO);

        rAttr.addFlashAttribute("successmessage", "게시글이 등록되었습니다.");

        System.out.println("========================================");
        System.out.println("게시글 등록 : " + postDTO);
        System.out.println("========================================");

        return "redirect:/board/detailpost/" + postId;
    }

    // 게시글 수정 페이지 이동
    @GetMapping("/editpost/{postId}")
    public String editPostForm(@PathVariable("postId") int postId,
                               Model model){

        PostDTO postDTO = postService.findDetailPostById(postId);
        model.addAttribute("postDTO", postDTO);

        System.out.println("========================================");
        System.out.println("게시글 수정으로 : " + postDTO);
        System.out.println("========================================");

        return "/board/editpost";
    }

    // 게시글 수정 후 상세페이지로 이동
    @PostMapping("/editpost/{postId}")
    public String updatePost(@PathVariable("postId") int postId,
                             @ModelAttribute PostDTO postDTO,
                             Authentication authentication,
                             RedirectAttributes rAttr,
                             @ModelAttribute ImageDTO newImage,
                             @RequestParam(required = false, name = "singleFile") MultipartFile singleFile) throws IOException {

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        postDTO.setUserId(userDTO.getId());
        postDTO.setNickname(userDTO.getNickname());
        postDTO.setPostId(postId);

        if(singleFile == null || singleFile.isEmpty()) {
            postDTO.setImageId(1);
        } else {
            System.out.println("[[파일 확인?]] = " + singleFile);

            Resource resource = resourceLoader.getResource("/static/image/");
            System.out.println("경로 확인쓰 = " + resource);

            String filePath = null;

            if (!resource.exists()) {

                //경로 없을 때
                String root = "/static/image/";

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
                    newImage.setImageId(postDTO.getImageId());
                } else {
                    System.out.println("파일 저장 안 됨!");
                }

                if(isFileSave) {

                    postService.registImage(newImage);

                    postDTO.setImageId(newImage.getImageId());

                    System.out.println("[DB에 저장 된 사진 경로?] = " + newImage);
                    System.out.println("업로드 성공!");

                } else {
                    System.out.println("[DB 저장 안 됨!!] : 경로에 파일 없음");
                }

            } catch (IOException e) {
                new File(filePath + "/" + savedName).delete();

                System.out.println("업로드 실패!");

                e.printStackTrace();

            }
        }

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
