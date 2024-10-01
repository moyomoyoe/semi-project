package moyomoyoe.image;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ImageMapper {

    void insertImage(ImageDTO imageDTO);

}
