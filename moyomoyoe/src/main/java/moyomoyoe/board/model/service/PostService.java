package moyomoyoe.board.model.service;

import moyomoyoe.board.model.dao.PostMapper;
import moyomoyoe.board.model.dto.CommentDTO;
import moyomoyoe.board.model.dto.KeywordDTO;
import moyomoyoe.board.model.dto.PostDTO;
import moyomoyoe.board.model.dto.RegionDTO;
import moyomoyoe.image.ImageDTO;
import moyomoyoe.member.auth.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PostService {

    private final PostMapper postMapper;

    @Autowired
    public PostService(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    // 날짜별 전체 게시글 목록
    public List<PostDTO> findAllPost() {
        return postMapper.findAllPost();
    }

    // 키워드 이름 목록
    public List<KeywordDTO> findKeywordName() {return postMapper.findKeywordName();}
    // 키워드별 게시글 목록
    public List<PostDTO> findKeywordList(int keywordId) {
        return postMapper.findKeywordList(keywordId);
    }

    // 시 선택 목록
    public List<RegionDTO> findRegionCityList() {
        List<RegionDTO> cityList= postMapper.findRegionCityList();
        return cityList;}
    // 구 선택 목록
    public List<RegionDTO> findRegionDistrictList(String city) {
        List<RegionDTO> districtList= postMapper.findRegionDistrictList(city);
        return districtList;
    }
    // 선택한 지역별 게시글 목록
    public List<PostDTO> findPostsByRegion(String city, int regionCode) {
        return postMapper.findPostsByRegion(city, regionCode);
    }

    // 제목검색 게시글 목록
    public List<PostDTO> findTitleList(String title) {
        List<PostDTO> titleList = postMapper.findTitleList(title);
        return titleList;
    }

    // postId 별 세부 게시글 내용
    public PostDTO findDetailPostById(int postId) {

        return postMapper.findDetailPostById(postId);
    }

    // postId 별 댓글 목록
    public List<CommentDTO> detailPostComment(int postId) {
        return postMapper.detailPostComment(postId);
    }

    // postId 별 세부 게시글 삭제
    @Transactional
    public void deletePost(int postId) {
        postMapper.deletePost(postId);
    }

    // 댓글 입력
    @Transactional
    public void comment(CommentDTO commentDTO) {
        postMapper.comment(commentDTO);
    }


    // 게시글 등록
    @Transactional
    public int createPost(PostDTO postDTO) {

        postMapper.insertPost(postDTO);
        return postDTO.getPostId();
    }

    // 게시글 수정
    @Transactional
    public void updatePost(PostDTO postDTO) {

        postMapper.updatePost(postDTO);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(int commentId) {

        postMapper.deleteComment(commentId);
    }

    public CommentDTO findCommentById(int commentId) {

        return postMapper.findCommentById(commentId);
    }

    public void registImage(ImageDTO newImage) {
        postMapper.registImage(newImage);
    }

    public ImageDTO getImageById(int imageId) {

        return postMapper.getImageById(imageId);
    }

    public int getImageByUserId(int userId) {
        return postMapper.getImageByUserId(userId);
    }


    public ImageDTO getProfileImageById(int imageId) {
        return postMapper.getProfileImageById(imageId);
    }

}
