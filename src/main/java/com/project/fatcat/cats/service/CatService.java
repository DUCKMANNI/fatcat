//
//package com.project.fatcat.cats.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.stereotype.Service;
//
//import com.project.fatcat.DataNotFoundException;
//import com.project.fatcat.cats.dto.CatAddDTO;
//import com.project.fatcat.cats.dto.CatUpdateDTO;
//import com.project.fatcat.cats.repository.CatRepository;
//import com.project.fatcat.entity.Cat;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class CatService {
//
//    private final CatRepository catRepository;
//
//
//    public List<Cat> findAll() {
//        return catRepository.findAll();
//    }
//
//    /**
//     * 고양이 정보를 고유 번호로 조회하는 메소드.
//     * @param catSeq 조회할 고양이의 고유 번호
//     * @return 고양이 정보 (Optional로 감싸져 있음)
//     */
//    public Optional<Cat> findById(Integer catSeq) {
//    	return this.catRepository.findById(catSeq);
//    }
//
//    public Cat save(Cat cat) {
//        return catRepository.save(cat);
//    }
//
//    public void deleteById(Integer catSeq) {
//        catRepository.deleteById(catSeq);
//    }
//
//    /**
//     * 새로운 고양이를 추가하는 메소드
//     * @param catAddDTO 추가할 정보가 담긴 DTO
//     * @return 저장된 고양이 엔티티
//     */
//    public Cat addCat(CatAddDTO catAddDTO) {
//        Cat cat = Cat.builder()
//                .catName(catAddDTO.getCatName())
//                .catBirtthday(catAddDTO.getCatBirtthday())
//                .catGender(catAddDTO.getCatGender())
//                .catBreed(catAddDTO.getCatBreed())
//                .isNeutered(catAddDTO.getIsNeutered())
//                .hasDisease(catAddDTO.getHasDisease())
//                .hasAllergy(catAddDTO.getHasAllergy())
////                .catImageFile(cat.CatImageFile())
//                .build();
//
//        return catRepository.save(cat);
//    }
//
//    
//    
//    /**
//     * 고양이 정보를 업데이트하는 메소드
//     * @param catSeq 업데이트할 고양이의 고유 번호
//     * @param catUpdateDTO 업데이트할 정보가 담긴 DTO
//     */
//    @Transactional // 트랜잭션 처리를 통해 데이터 변경 작업을 안전하게 실행
//    public void updateCat(Integer catSeq, CatUpdateDTO catUpdateDTO) {
//        // 1. catSeq를 사용하여 기존 Cat 엔티티를 찾습니다.
//        Cat cat = findById(catSeq)
//            .orElseThrow(() -> new DataNotFoundException("Cat not found with id: " + catSeq));
//           
//
//        // 2. DTO의 정보로 엔티티의 필드를 업데이트합니다.
//        cat.setCatName(catUpdateDTO.getCatName());
//        cat.setCatBirtthday(catUpdateDTO.getCatBirtthday());
////        cat.setCatImageUrl(catUpdateDTO.getCatImageUrl());
//        cat.setCatGender(catUpdateDTO.getCatGender());
//        cat.setCatBreed(catUpdateDTO.getCatBreed());
//        cat.setNeutered(catUpdateDTO.getIsNeutered());
//        cat.setHasDisease(catUpdateDTO.getHasDisease());
//        cat.setHasAllergy(catUpdateDTO.getHasAllergy());
//        
//        // 3. 명시적으로 save() 메서드를 호출하여 변경사항을 DB에 저장합니다.
//        this.catRepository.save(cat);
//    }
//
//    /**
//     * 특정 사용자의 고양이 리스트를 조회하는 메소드
//     * @param userId 로그인한 사용자의 고유 ID
//     * @return 해당 사용자가 키우는 고양이들의 리스트
//     */
//    public List<Cat> findAllByUserId(Integer userId) {
//        return catRepository.findByUserUserSeq(userId);
//    }
//}




package com.project.fatcat.cats.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.fatcat.DataNotFoundException;
import com.project.fatcat.cats.dto.CatAddDTO;
import com.project.fatcat.cats.dto.CatUpdateDTO;
import com.project.fatcat.cats.repository.CatRepository;
import com.project.fatcat.entity.Cat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.project.fatcat.upload.fatcatSftp;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatService {


    private final CatRepository catRepository;

    
    
 // application.properties에서 파일 업로드 경로를 주입받습니다.
    @Value("${file.upload.dir}")
    private String uploadDir;
    
    
    @Transactional
    public void updateCat(Integer catSeq, CatUpdateDTO catUpdateDTO) {
        Optional<Cat> catOptional = findById(catSeq);
        
        if (catOptional.isPresent()) {
            Cat cat = catOptional.get();
            String savedImageUrl = cat.getCatImageUrl();
    
            MultipartFile catImageFile = catUpdateDTO.getCatImageFile();
            if (catImageFile != null && !catImageFile.isEmpty()) {
                try {
                		
                		fatcatSftp fatcatSftp = new fatcatSftp();
                		fatcatSftp.sftpFileUpload(catImageFile);
                	
                		/* 유정쓰가 만든거 잠깐 주석~~~~
                    // 1. 파일 저장 경로 설정 및 디렉토리 생성
                    Path uploadPath = Paths.get(uploadDir); // 변경된 부분
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
    
                    // 2. 파일명 고유하게 만들기 (UUID 사용)
                    String originalFilename = catImageFile.getOriginalFilename();
                    String extension = "";
                    if (originalFilename != null && originalFilename.contains(".")) {
                        extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    }
                    String savedFilename = UUID.randomUUID().toString() + extension;
                    
                    System.out.println(savedFilename);
    
                    // 3. 서버에 파일 저장
                    Path filePath = uploadPath.resolve(savedFilename);
                    catImageFile.transferTo(filePath.toFile());
    
                    // 4. 기존 이미지 파일 삭제
                    if (savedImageUrl != null && !savedImageUrl.isEmpty() && !savedImageUrl.contains("default-cat.png")) {
                        try {
                            String oldFilename = savedImageUrl.substring(savedImageUrl.lastIndexOf("/") + 1);
                            Files.deleteIfExists(Paths.get(uploadDir, oldFilename));
                        } catch (IOException e) {
                            log.error("Failed to delete old image file: " + savedImageUrl, e);
                        }
                    }
                    */
    
                    // 5. DB에 저장될 URL 업데이트 (웹에서 접근 가능한 URL)
                    //savedImageUrl = "/images/cat-profiles/" + savedFilename;
    
                } catch (IOException e) {
                    log.error("Failed to upload image file", e);
                } catch (Exception ee) {
                		
                }
            }
    
            // 6. DTO의 정보로 엔티티의 필드 업데이트
            cat.setCatName(catUpdateDTO.getCatName());
            cat.setCatBirtthday(catUpdateDTO.getCatBirtthday());
            cat.setCatGender(catUpdateDTO.getCatGender());
            cat.setCatBreed(catUpdateDTO.getCatBreed());
            cat.setNeutered(catUpdateDTO.getIsNeutered());
            cat.setHasDisease(catUpdateDTO.getHasDisease());
            cat.setHasAllergy(catUpdateDTO.getHasAllergy());
            cat.setCatImageUrl(savedImageUrl);
    
            // 7. 변경사항을 DB에 저장
            this.catRepository.save(cat);
        } else {
            throw new DataNotFoundException("고양이를 찾을 수 없습니다.");
        }
    }
    
    
    
    
    // TODO: 실제 운영 환경에서는 별도 설정 파일로 관리해야 함.
//    private final String IMAGE_UPLOAD_DIR = "*";

    public List<Cat> findAll() {
        return catRepository.findAll();
    }

    /**
     * 고양이 정보를 고유 번호로 조회하는 메소드.
     * @param catSeq 조회할 고양이의 고유 번호
     * @return 고양이 정보 (Optional로 감싸져 있음)
     */
    public Optional<Cat> findById(Integer catSeq) {
    	return this.catRepository.findById(catSeq);
    }

    public Cat save(Cat cat) {
        return catRepository.save(cat);
    }

    public void deleteById(Integer catSeq) {
        catRepository.deleteById(catSeq);
    }

    /**
     * 새로운 고양이를 추가하는 메소드
     * @param catAddDTO 추가할 정보가 담긴 DTO
     * @return 저장된 고양이 엔티티
     */
    public Cat addCat(CatAddDTO catAddDTO) {
        Cat cat = Cat.builder()
                .catName(catAddDTO.getCatName())
                .catBirtthday(catAddDTO.getCatBirtthday())
                .catGender(catAddDTO.getCatGender())
                .catBreed(catAddDTO.getCatBreed())
                .isNeutered(catAddDTO.getIsNeutered())
                .hasDisease(catAddDTO.getHasDisease())
                .hasAllergy(catAddDTO.getHasAllergy())
//                .catImageUrl(catAddDTO.getCatImageUrl())
                .build();

        return catRepository.save(cat);
    }
    
 

    /**
     * 특정 사용자의 고양이 리스트를 조회하는 메소드
     * @param userId 로그인한 사용자의 고유 ID
     * @return 해당 사용자가 키우는 고양이들의 리스트
     */
    public List<Cat> findAllByUserId(Integer userId) {
        return catRepository.findByUserUserSeq(userId);
    }
}
