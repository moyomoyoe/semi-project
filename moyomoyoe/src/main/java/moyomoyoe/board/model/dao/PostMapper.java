package moyomoyoe.board.model.dao;

import moyomoyoe.board.model.dto.PostDTO;
import moyomoyoe.board.model.dto.RegionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    List<PostDTO> findAllPost();

    List<PostDTO> findKeywordList(@Param("keywordId") int keywordId);

    List<RegionDTO> findRegionCityList();

    List<RegionDTO> findRegionDistrictList(@Param("city") String city);

    List<PostDTO> findTitleList(@Param("title") String title);

    List<PostDTO> findPostsByRegion(@Param("city") String city, @Param("regionCode") int regionCode);

    PostDTO findDetailPostById(@Param("postId") int postId);

    void deletePost(int postId);
}
