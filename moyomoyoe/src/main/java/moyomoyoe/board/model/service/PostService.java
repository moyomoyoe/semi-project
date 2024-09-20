package moyomoyoe.board.model.service;

import moyomoyoe.board.model.dao.PostMapper;
import moyomoyoe.board.model.dto.PostDTO;
import moyomoyoe.board.model.dto.RegionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PostService {

    private final PostMapper postMapper;

    @Autowired
    public PostService(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public List<PostDTO> findAllPost() {
        return postMapper.findAllPost();
    }

    public List<PostDTO> findKeywordPost(int keywordId) {
        return postMapper.findKeywordPost(keywordId);
    }

    public List<RegionDTO> findRegionPost() {
        return postMapper.findRegionPost();
    }
}
