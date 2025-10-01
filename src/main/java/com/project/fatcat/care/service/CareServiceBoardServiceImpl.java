package com.project.fatcat.care.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.fatcat.DataNotFoundException;
import com.project.fatcat.care.dto.CareServiceBoardDto;
import com.project.fatcat.care.dto.CareServiceBoardListDto;
import com.project.fatcat.care.repository.CareServiceBoardRepository;
import com.project.fatcat.entity.CareServiceBoard;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CareServiceBoardServiceImpl implements CareServiceBoardService {

    private final CareServiceBoardRepository careServiceBoardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void save(CareServiceBoardDto careServiceBoardDto) {
    	
        Optional<User> userOptional = userRepository.findById(careServiceBoardDto.getUserSeq());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            CareServiceBoard board = careServiceBoardDto.toEntity();

            board.setUser(user);
            board.setCreateDate(LocalDateTime.now());
            board.setUpdateDate(LocalDateTime.now());

            careServiceBoardRepository.save(board);
        } else {
            throw new IllegalArgumentException("Invalid user sequence");
        }
    }
 
    @Override
    public List<CareServiceBoardListDto> getAllBoardsAsDto() {
        List<CareServiceBoard> boards = careServiceBoardRepository.findAll();
        return boards.stream()
            .map(board -> new CareServiceBoardListDto(
                board.getCareSeq(),
                board.getCareTitle(),
                board.getCareContent(),
                board.getAddress1(),
                board.getAddress2(),
                board.getLatitude(),
                board.getLongitude(),
                board.getPrice(),
                board.getUser() != null ? board.getUser().getNickname() : "알 수 없음",
                board.getCreateDate(),
                board.getUser() != null ? board.getUser().getUserSeq() : null
            ))
            .collect(Collectors.toList());
    }

    @Override
    public List<CareServiceBoardListDto> getBoardsByRegionAsDto(String region) {
    	
        List<CareServiceBoard> boards = careServiceBoardRepository.findByAddress1StartingWith(region);
        return boards.stream()
            .map(board -> new CareServiceBoardListDto(
                board.getCareSeq(),
                board.getCareTitle(),
                board.getCareContent(),
                board.getAddress1(),
                board.getAddress2(),
                board.getLatitude(),
                board.getLongitude(),
                board.getPrice(),
                board.getUser() != null ? board.getUser().getNickname() : "알 수 없음",
                board.getCreateDate(),
                board.getUser() != null ? board.getUser().getUserSeq() : null
            ))
            .collect(Collectors.toList());
    }

    @Override
    public List<CareServiceBoardListDto> getBoardsWithinRadiusAsDto(Double latitude, Double longitude, double radiusInKm) {
        // 위치 기반 검색 로직을 여기에 구현하고 DTO로 변환하여 반환
        // 예시로 findAll() 사용
        List<CareServiceBoard> boards = careServiceBoardRepository.findAll(); 
        
        return boards.stream()
            .map(board -> new CareServiceBoardListDto(
                board.getCareSeq(),
                board.getCareTitle(),
                board.getCareContent(),
                board.getAddress1(),
                board.getAddress2(),
                board.getLatitude(),
                board.getLongitude(),
                board.getPrice(),
                board.getUser() != null ? board.getUser().getNickname()  : "알 수 없음",
                board.getCreateDate(),
                board.getUser() != null ? board.getUser().getUserSeq() : null
            ))
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteBoard(Integer careSeq, Integer userSeq) {
        CareServiceBoard careServiceBoard = getBoard(careSeq);
        
        // 본인 글만 삭제 가능
        if (!careServiceBoard.getUser().getUserSeq().equals(userSeq)) {
            throw new SecurityException("본인 글만 삭제할 수 있습니다.");
            
        }
        this.careServiceBoardRepository.delete(careServiceBoard);
    }
    
    @Override
    public CareServiceBoard getBoard(Integer careSeq) {
        Optional<CareServiceBoard> cb = this.careServiceBoardRepository.findById(careSeq);
        
        if(cb.isPresent()) {
            return cb.get();
        } else {
            throw new DataNotFoundException("해당 돌봄서비스를 찾을수 없습니다.");
        }
    }

  
    @Override
    @Transactional
    public void modify(Integer careSeq, CareServiceBoardDto careServiceBoardDto, Integer userSeq) {
       
        CareServiceBoard careBoard = getBoard(careSeq);
        
     // 본인 글만 수정 가능
        if (!careBoard.getUser().getUserSeq().equals(userSeq)) {
            throw new SecurityException("본인 글만 수정할 수 있습니다.");
        }

        // 2. DTO의 필드 값을 엔티티에 복사합니다.
        careBoard.setCareTitle(careServiceBoardDto.getCareTitle());
        careBoard.setCareContent(careServiceBoardDto.getCareContent());
        careBoard.setAddress1(careServiceBoardDto.getAddress1());
        careBoard.setAddress2(careServiceBoardDto.getAddress2());
        careBoard.setLatitude(careServiceBoardDto.getLatitude());
        careBoard.setLongitude(careServiceBoardDto.getLongitude());
        careBoard.setPrice(careServiceBoardDto.getPrice());
        careBoard.setUpdateDate(LocalDateTime.now());

    }
}