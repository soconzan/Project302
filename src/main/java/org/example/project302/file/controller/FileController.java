package org.example.project302.file.controller;

import lombok.RequiredArgsConstructor;
import org.example.project302.file.dto.FileResponse;
import org.example.project302.file.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    /*
     *  파일 조회 .-≡=★
     */
    @GetMapping("/{name}")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(@PathVariable("name") String fileId) {
        FileResponse res = fileService.getFile(fileId);
        if (res.getContentType() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", res.getContentType());
            return new ResponseEntity<>(res.getBytes(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     *  s3 파일 조회
     */
//    @GetMapping("/{name}")
//    @ResponseBody
//    public String getFile(@PathVariable("name") String fileId) {
//        String res = fileService.getFileLink(fileId);
//        return res;
//    }
}
