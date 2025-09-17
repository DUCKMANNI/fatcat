package com.project.fatcat.care.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.fatcat.care.dto.CareServiceBoardDto;
import com.project.fatcat.care.repository.CareServiceBoardRepository;
import com.project.fatcat.entity.CareServiceBoard;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.UserRepository;

@Service
public class CareServiceBoardService {

    @Autowired
    private CareServiceBoardRepository careServiceBoardRepository;
    
    @Autowired
    private UserRepository userRepository; // User 엔티티를 가져오기 위한 Repository

    public void save(CareServiceBoardDto careServiceBoardDto) {
        // DTO에서 userSeq를 사용하여 User 엔티티 조회
        Optional<User> userOptional = userRepository.findById(careServiceBoardDto.getUserSeq());
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // DTO를 엔티티로 변환
            CareServiceBoard board = careServiceBoardDto.toEntity();
            
            // 필수 정보 추가
            board.setUser(user);
            board.setCreateDate(LocalDateTime.now());
            board.setUpdateDate(LocalDateTime.now());
            
            // 엔티티 저장
            careServiceBoardRepository.save(board);
        } else {
            // 사용자를 찾을 수 없을 경우 예외 처리
            throw new IllegalArgumentException("Invalid user sequence");
        }
           
    }
    
 // ⭐️ 새로 추가된 메서드 ⭐️

    // 모든 돌봄 게시글을 조회하는 메서드
    public List<CareServiceBoard> getAllBoards() {
        return careServiceBoardRepository.findAll();
    }
    
    // 특정 지역(address1)으로 돌봄 게시글을 조회하는 메서드
    public List<CareServiceBoard> getBoardsByRegion(String region) {
        // 'address1' 필드로 검색하는 리포지토리 메서드가 필요합니다.
        // 아래에 추가 설명을 참조하세요.
        return careServiceBoardRepository.findByAddress1StartingWith(region);
    }

	public List<CareServiceBoard> getBoardsWithinRadius(Double latitude, Double longitude, double radiusInKm) {
		// TODO Auto-generated method stub
		return null;
	}
    
    
}