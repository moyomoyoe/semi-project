package moyomoyoe.board.model.dao;

import moyomoyoe.board.model.dto.CommentDTO;
import moyomoyoe.board.model.dto.KeywordDTO;
import moyomoyoe.board.model.dto.PostDTO;
import moyomoyoe.board.model.dto.RegionDTO;
import moyomoyoe.image.ImageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    // 날짜별 전체게시글 목록
    List<PostDTO> findAllPost();

    // 키워드 선택 목록
    List<KeywordDTO> findKeywordName();
    // 키워드별 게시글 목록
    List<PostDTO> findKeywordList(@Param("keywordId") int keywordId);

    // 시 선택 목록
    List<RegionDTO> findRegionCityList();
    // 구 선택 목록
    List<RegionDTO> findRegionDistrictList(@Param("city") String city);
    // 선택한 지역별 게시글 목록
    List<PostDTO> findPostsByRegion(@Param("city") String city, @Param("regionCode") int regionCode);

    // 제목검색 게시글 목록
    List<PostDTO> findTitleList(@Param("title") String title);

    // postId별 세부게시글내용
    PostDTO findDetailPostById(@Param("postId") int postId);
    // postId 별 댓글 목록
    List<CommentDTO> detailPostComment(int postId);
    // postId 별 게시글 삭제
    void deletePost(int postId);
    // postId 별 댓글 삽입
    void comment(CommentDTO commentDTO);


    // 게시글 등록
    void insertPost(PostDTO postDTO);

    // 게시글 수정
    void updatePost(PostDTO postDTO);

    // 게시글 댓글 삭제
    void deleteComment(int commentId);

    // 게시글 댓글 삭제 권한
    CommentDTO findCommentById(int commentId);

    // 이미지 첨부
    void registImage(ImageDTO newImage);

    ImageDTO getImageById(int imageId);

    // 프로필 이미지 가져오기
    ImageDTO getProfileImageById(int imageId);

    // userId로 imageId 조회 (프로필)
    int getImageByUserId(int userId);
}
