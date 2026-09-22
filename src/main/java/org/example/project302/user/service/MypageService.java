package org.example.project302.user.service;

import com.univcert.api.UnivCert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.file.service.FileService;
import org.example.project302.univ.University;
import org.example.project302.univ.UniversityRepository;
import org.example.project302.user.dto.EditprofileReq;
import org.example.project302.user.dto.EditprofileRes;
import org.example.project302.user.dto.UnivInfo;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MypageService {
    private final FileService fileService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;

    @Value("${univcert.api.key}")
    private String univCertAPIKey;

    // 사진 업로드
    public String profileUpload(MultipartFile file, Principal principal) {
        User user = userService.findById(principal.getName());

        // 기존 파일 삭제
        if (user.getUserFileId() != null) {
            fileService.fileDelete(user.getUserFileId());
        }

        // 새로운 파일 업로드 및 파일 ID 설정
        String uploadedFileName = fileService.uploadFile(file);
        if (uploadedFileName != null) {
            user.setUserFileId(uploadedFileName);
            userRepository.save(user);
        } else {
            // 파일 업로드 실패 시 처리 (필요에 따라 예외 처리)
            log.error("File upload failed for user: {}", user.getLocalId());
            throw new RuntimeException("File upload failed");
        }

        // 파일 링크 가져오기
        String fileLink = fileService.getFileLink(user);
        log.info("fileLink = {}", fileLink);

        return fileLink;
    }

    // 사진 삭제
    public String profileDelete(Principal principal) {
        User user = userService.findById(principal.getName());

        // 기존 파일 ID가 있는 경우 파일 삭제
        if (user.getUserFileId() != null) {
            try {
                fileService.fileDelete(user.getUserFileId());
            } catch (Exception e) {
                log.error("Error deleting file for user: {}", user.getLocalId(), e);
                throw new RuntimeException("File deletion failed");
            }
        }

        // 파일 ID를 null로 설정하고 사용자 정보 저장
        user.setUserFileId(null);
        userRepository.save(user);

        // 파일 링크 가져오기
        String fileLink = fileService.getFileLink(user);
        log.info("Deleted profile image, new fileLink = {}", fileLink);

        return fileLink;
    }

    // 마이페이지 유저 정보
    public EditprofileRes getEditprofile(Principal principal) {
        User user = userService.findById(principal.getName());
        return new EditprofileRes(user, fileService.getFileLink(user));
    }

    // 개인 정보 수정 정보 저장
    public void updateProfile(EditprofileReq editprofileReq,Principal principal){
        User user = userService.findById(principal.getName());
        user.setPhoneNo(editprofileReq.getPhoneNo());
        user.setNickname(editprofileReq.getNickname());
        user.setBirth(editprofileReq.getBirth());
        user.setEmail(editprofileReq.getEmail());
        user.setBank(editprofileReq.getBank());
        user.setAccountNo(editprofileReq.getAccountNo());
        user.setUserName(editprofileReq.getUserName());
        userRepository.save(user);
    }

    // 대학 검색
    public List<University> univNameSearch(String univName) throws IOException {
        List<University> universityList = universityRepository.findByUniversityNameContaining(univName);
        // univcert에서 인증가능 대학인지 확인
        List<University> checkUnivList = new ArrayList<>();
        for (University univ:universityList) {
            if((boolean)UnivCert.check(univ.getUnivName()).get("success")){
                checkUnivList.add(univ);
            }
        }
        // 되는 학교만 반환
        return checkUnivList;
    }

    // 학교 도메인 검증 안해줌
    public boolean univEmailCheck(UnivInfo univInfo) throws IOException {
        Map<String, Object> certify = UnivCert.certify(univCertAPIKey, univInfo.getEmailUniv(), univInfo.getNameUniv(), true);
        log.info("univCert 확인 = {}",certify);
        return (boolean) certify.get("success");
    }


    public void univAuth(UnivInfo univInfo, Principal principal) {
        User user = userService.findById(principal.getName());
        University university = universityRepository.findById(univInfo.getIdUniv())
                .orElseThrow(() -> new IllegalArgumentException("그런 대학은 없어요"));
        user.setEmailYn(true);
        user.setUniversity(university);
        userRepository.save(user);
    }
}
