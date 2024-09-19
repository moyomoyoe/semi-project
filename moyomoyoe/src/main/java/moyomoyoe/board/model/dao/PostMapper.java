package moyomoyoe.board.model.dao;

import moyomoyoe.board.model.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    List<PostDTO> findAllPost();
}
