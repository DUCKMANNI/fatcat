

package com.project.fatcat.cats.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.fatcat.DataNotFoundException;
import com.project.fatcat.SecurityUtils;
import com.project.fatcat.cats.dto.CatAddDTO;
import com.project.fatcat.cats.dto.CatUpdateDTO;
import com.project.fatcat.cats.repository.CatRepository;
import com.project.fatcat.entity.Cat;
import com.project.fatcat.upload.fatcatSftp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatService {


    private final CatRepository catRepository;

    
    private static final String UPLOAD_DIR_CATIMAGE = "//fatcat/upload/catImage/";
    
    @Transactional
    public void updateCat(Integer catSeq, CatUpdateDTO catUpdateDTO) {
        Optional<Cat> catOptional = catRepository.findById(catSeq);
        
        if (catOptional.isPresent()) {
            Cat cat = catOptional.get();
            String savedImageUrl = cat.getCatImageUrl();
    
            MultipartFile catImageFile = catUpdateDTO.getCatImageFile();
            
            if (catImageFile != null && !catImageFile.isEmpty()) {
                try {
                		
                		fatcatSftp fatcatSftp = new fatcatSftp();
                		fatcatSftp.sftpFileUpload(catImageFile);

    
                } catch (IOException e) {
                    log.error("Failed to upload image file", e);
                } catch (Exception ee) {
                		
                }
            }
    
            // 6. DTO의 정보로 엔티티의 필드 업데이트
            cat.setCatName(catUpdateDTO.getCatName());
            cat.setCatBirthday(catUpdateDTO.getCatBirthday());
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
    
    
    public Cat addCat(CatAddDTO catAddDTO) {
    	
    	System.out.println("getCustomBreed : " + catAddDTO.getCustomBreed());
    	
    	 String breed = catAddDTO.getCatBreed();
         if ("기타".equals(breed)) {
             breed = catAddDTO.getCustomBreed();
         }
         
         String imageUrl = saveFile(catAddDTO.getCatImageFile(),UPLOAD_DIR_CATIMAGE);
    	
        log.info("imageUrl : " + imageUrl);
        System.out.println("imageUrl : " + imageUrl);
         
        Cat cat = Cat.builder()
        		.user(SecurityUtils.getCurrentUser().getUser())
                .catName(catAddDTO.getCatName())
                .catBirthday(catAddDTO.getCatBirthday())
                .catGender(catAddDTO.getCatGender())
                .catBreed(breed)
                .isNeutered(catAddDTO.getIsNeutered())
                .hasDisease(catAddDTO.getHasDisease())
                .hasAllergy(catAddDTO.getHasAllergy())
                .catImageUrl(imageUrl)
                .build();

        return catRepository.save(cat);
    }
    

    public List<String> getAllBreeds() {
        // 실제로는 DB에서 가져오거나 ENUM으로 관리 가능
        return List.of( "코리안 숏헤어", "페르시안", "샴", "러시안 블루", 
                "아메리칸 숏헤어", "스코티시 폴드", "벵갈", "노르웨이 숲 고양이",
                "메인 쿤", "아비시니안", "터키시 앙고라", "스핑크스",
                "렉돌", "브리티시 숏헤어", "먼치킨", "시베리안",
                "뱅갈", "데본 렉스", "아메리칸 컬");
    }
    
    private String saveFile(MultipartFile file, String uploadDir) {
        if (file == null || file.isEmpty()) return null;

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            fatcatSftp fatcatSftp = new fatcatSftp();
            fatcatSftp.sftpFileUpload(file, uploadDir, fileName);
            // 웹에서 접근 가능한 경로 리턴
            return "https://ivisus.duckdns.org:9443" + uploadDir + fileName;

        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }
 

    public List<Cat> findAllByUserId(Integer userId) {
        return catRepository.findByUserUserSeq(userId);
    }
}
