package com.project.fatcat.cats.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.fatcat.cats.dto.CatUpdateDTO;
import com.project.fatcat.cats.repository.CatRepository;
import com.project.fatcat.entity.Cat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatService {

    private final CatRepository catRepository;


    public List<Cat> findAll() {
        return catRepository.findAll();
    }

    public Optional<Cat> findById(Integer catSeq) {
        return catRepository.findById(catSeq);
    }

    public Cat save(Cat cat) {
        return catRepository.save(cat);
    }

    public void deleteById(Integer catSeq) {
        catRepository.deleteById(catSeq);
    }

    /**
     * 고양이 정보를 업데이트하는 메소드
     * @param catSeq 업데이트할 고양이의 고유 번호
     * @param catUpdateDTO 업데이트할 정보가 담긴 DTO
     */
    @Transactional // 트랜잭션 처리를 통해 데이터 변경 작업을 안전하게 실행
    public void updateCat(Integer catSeq, CatUpdateDTO catUpdateDTO) {
        // 1. catSeq를 사용하여 기존 Cat 엔티티를 찾습니다.
        //    만약 찾지 못하면 IllegalArgumentException을 발생시킵니다.
        Cat cat = findById(catSeq)
            .orElseThrow(() -> new IllegalArgumentException("Invalid catSeq:" + catSeq));

        // 2. DTO의 정보로 엔티티의 필드를 업데이트합니다.
        cat.setCatName(catUpdateDTO.getCatName());
        cat.setCatBirtthday(catUpdateDTO.getCatBirtthday());
        cat.setCatImageUrl(catUpdateDTO.getCatImageUrl());
        cat.setCatGender(catUpdateDTO.getCatGender());
        cat.setCatBreed(catUpdateDTO.getCatBreed());
        cat.setNeutered(catUpdateDTO.getIsNeutered());
        cat.setHasDisease(catUpdateDTO.getHasDisease());
        cat.setHasAllergy(catUpdateDTO.getHasAllergy());
    }

    /**
     * 특정 사용자의 고양이 리스트를 조회하는 메소드
     * @param userId 로그인한 사용자의 고유 ID
     * @return 해당 사용자가 키우는 고양이들의 리스트
     */
    public List<Cat> findAllByUserId(Integer userId) {
        // 1. userSeq를 기반으로 CatRepository에 고양이 리스트 조회를 요청합니다.
        //    CatRepository에 findByUserId(Integer userId) 메소드를 추가해야 합니다.
        return catRepository.findByUserUserSeq(userId);
    }
}