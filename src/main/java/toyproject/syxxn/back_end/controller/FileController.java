package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.service.file.FileService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/file")
@RestController
public class FileController {

    private final FileService fileService;

    @PostMapping("/post/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveFiles(@PathVariable("id") Integer postId,
                          @RequestParam List<MultipartFile> files) {
        fileService.saveFiles(postId, files);
    }

}
