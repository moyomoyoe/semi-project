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

    public List<PostDTO> findKeywordList(int keywordId) {
        return postMapper.findKeywordList(keywordId);
    }

    public List<RegionDTO> findRegionCityList() {
        List<RegionDTO> cityList= postMapper.findRegionCityList();

        return cityList;}

    public List<RegionDTO> findRegionDistrictList(String city) {
        List<RegionDTO> districtList= postMapper.findRegionDistrictList(city);
        return districtList;
    }

    public List<PostDTO> findTitleList(String title) {
        List<PostDTO> titleList = postMapper.findTitleList(title);
        System.out.println("검색된 titleList 데이터: " + titleList);
        return titleList;
    }
}
