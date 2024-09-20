package moyomoyoe.board.model.dao;

import moyomoyoe.board.model.dto.PostDTO;
import moyomoyoe.board.model.dto.RegionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    List<PostDTO> findAllPost();

    List<PostDTO> findKeywordPost(@Param("keywordId") int keywordId);

    List<RegionDTO> findRegionPost();
}
