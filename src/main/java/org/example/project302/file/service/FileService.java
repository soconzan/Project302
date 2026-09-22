package org.example.project302.file.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.file.dto.FileResponse;
import org.example.project302.file.entity.File;
import org.example.project302.file.repository.FileRepository;
import org.example.project302.group.dto.FileSaveForm;
import org.example.project302.product.entity.Product;
import org.example.project302.product.repository.ProductRepository;
//import org.example.project302.s3.config.S3Client;
import org.example.project302.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final AmazonS3 amazonS3;

    private final FileRepository fileRepository;

    @Value("${file.dir}")    // 저장할 폴더 경로
    private String fileDir;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일리스트 저장
    /*public List<File> uploadFile(List<MultipartFile> files, Product product) {
        List<File> imgList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            if (!files.isEmpty()) {
                String uuid = UUID.randomUUID().toString();
                String savedName = uuid + "_" + files.get(i).getOriginalFilename(); // uuid를 사용하여 저장명 생성
//                java.io.File dest = new java.io.File(fileDir, savedName);
                try {
                    // s3 저장
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(files.get(i).getSize());
                    metadata.setContentType(files.get(i).getContentType());
                    awsS3Config.amazonS3().putObject(new PutObjectRequest(bucketName, savedName, files.get(i).getInputStream(), metadata));
                    String uploadFileUrl = awsS3Config.amazonS3().getUrl(bucketName, savedName).toString();
                    System.out.println(uploadFileUrl + "에스");

//                    files.get(i).transferTo(dest); // 지정한 폴더 경로에 새로운 저장명으로 파일 전송
                    boolean thumbnailYn = i == files.size() - 1;
                    imgList.add(new File(savedName, fileDir, thumbnailYn, product));
                    // 썸네일
                    Path savePath = Paths.get(savedName);
                    String thumbnailSave = "s_" + savedName;
                    Thumbnailator.createThumbnail(new java.io.File(fileDir, savePath.toString()), new java.io.File(fileDir, thumbnailSave), 100, 100);
                    System.out.println(imgList + "imgList");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("보자");
        return imgList;
    }*/

    // S3 이미지 저장
    public List<String> s3Upload(List<MultipartFile> multipartFile) throws IOException {
        List<String> imgs = new ArrayList<>();
        if (multipartFile == null || multipartFile.isEmpty()) {
            return imgs;
        } else {
            for (MultipartFile file : multipartFile) {
                if (!amazonS3.doesObjectExist(bucket, file.getOriginalFilename())) {
                    log.info(file.getOriginalFilename() + "젭알");
                    String uuid = UUID.randomUUID().toString();
                    String savedName = uuid + "_" + file.getOriginalFilename();
                    // s3 저장
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(file.getSize());
                    metadata.setContentType(file.getContentType());

                    try (InputStream inputStream = file.getInputStream()) {
                        amazonS3.putObject(new PutObjectRequest(bucket, savedName, inputStream, metadata));
                        String uploadFileUrl = amazonS3.getUrl(bucket, savedName).toString();
                        System.out.println(uploadFileUrl + "에스");
                        imgs.add(savedName);
                    } catch (IOException e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
                    }
                } else {
                    imgs.add(file.getOriginalFilename());
                }
            }
        }
        log.info(imgs.toString() + "파일 뭐임");
        return imgs;
    }


    /*
     *  S3 파일 저장 .-≡=★
     */
    public String uploadFile(MultipartFile file) {

        if (!file.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String savedName = uuid + "_" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            try {
                amazonS3.putObject(bucket, savedName, file.getInputStream(), metadata);
                return savedName;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }


    // 파일 불러오기
    public FileResponse getFile(String fileId) {
        FileResponse res = new FileResponse();
        try {
            java.io.File file = new java.io.File(fileDir, fileId); // name으로 폴더 내에서 파일 조회
            res.setBytes(FileCopyUtils.copyToByteArray(file)); // 파일의 byte 준비
            res.setContentType(Files.probeContentType(file.toPath())); // 파일 type과 함께 반환
        } catch (Exception e) {
        }
        return res;
    }


    // 파일 삭제
    public void fileDelete(String fileName) {
        log.info("파일 이름 : " + fileName);
        try {
            amazonS3.deleteObject(this.bucket, fileName);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }


    /*
     *  파일 링크 불러오기 .-≡=★
     */
    public String getFileLink(String fileId) {
        return amazonS3.getUrl(bucket, fileId).toString();
    }



    /*
     *  Product - 파일 링크 불러오기 .-≡=★
     */
    public String getFileLink(Product product) {
        return product.getCategory().getCtgrId() == 12 ?
                getOttFileLink(product) :
                getFileLink(fileRepository.findByProductAndThumbnailYnTrue(product)
                        .map(File::getFileId)
                        .orElse("no-product.png"));
    }

    /*
     *  User - 파일 링크 불러오기 .-≡=★
     */
    public String getFileLink(User user) {
        return getFileLink(Optional.ofNullable(user.getUserFileId()).orElse("no-profile.png"));
    }

    /*
     *  OTT 공구 상품 썸네일 불러오기
     */
    private String getOttFileLink(Product product) {
        if (product.getPdName().startsWith("넷플릭스"))
            return getFileLink("logo-netflix.png");
        else if (product.getPdName().startsWith("웨이브"))
            return getFileLink("logo-wavve.png");
        else if (product.getPdName().startsWith("티빙"))
            return getFileLink("logo-tving.png");
        else if (product.getPdName().startsWith("디즈니"))
            return getFileLink("logo-disneyplus.png");
        else if (product.getPdName().startsWith("쿠팡"))
            return getFileLink("logo-coupangplay.png");
        else
            return getFileLink("logo-watcha.png");
    }
}
