package moyomoyoe.board.controller;

import moyomoyoe.board.model.service.PostService;
import moyomoyoe.image.ImageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ImageController {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private PostService postService;

    @PostMapping("/board/detailpost/single-file")
    public String singleFileUpload(@RequestParam(required = false, name = "singleFile") MultipartFile singleFile,
                                   @RequestParam ImageDTO newImage,
                                   RedirectAttributes rAttr) throws IOException {

        Resource resource = resourceLoader.getResource("/static/image/");

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

        try {
            singleFile.transferTo(new File(filePath + "/" + savedName));

            postService.registImage(newImage);

            rAttr.addFlashAttribute("message", "성공");
            rAttr.addFlashAttribute("img", "/static/image/" + savedName);

            System.out.println("업로드 성공!");

        } catch(IOException e) {
            new File(filePath + "/" + savedName).delete();

            rAttr.addFlashAttribute("message", "실패");

            System.out.println("업로드 실패!");

            e.printStackTrace();
        }

        return "redirect:/board/createpost";
    }
}
