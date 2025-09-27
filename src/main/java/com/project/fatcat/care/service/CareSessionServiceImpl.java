package com.project.fatcat.care.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.care.repository.CareSessionRepository;
import com.project.fatcat.entity.CareSession;
import com.project.fatcat.entity.User;
import com.project.fatcat.entity.enums.CareSessionStatus;
import com.project.fatcat.users.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CareSessionServiceImpl implements CareSessionService {

    private final CareSessionRepository careSessionRepository;
    private final UserRepository userRepository;

    // 1. 돌봄 요청 생성
    @Override
    @Transactional
    public CareSessionDto createSession(CareSessionDto dto) {
        CareSession session = new CareSession();
        session.setStartDate(dto.getStartDate());
        session.setEndDate(dto.getEndDate());
        session.setStatus(CareSessionStatus.REQUESTED);

        User owner = userRepository.findById(dto.getOwnerUserId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        User sitter = userRepository.findById(dto.getSitterUserId())
                .orElseThrow(() -> new IllegalArgumentException("Sitter not found"));

        session.setOwnerUser(owner);
        session.setSitterUser(sitter);

        CareSession saved = careSessionRepository.save(session);

        return CareSessionDto.builder()
                .id(saved.getSessionSeq())
                .ownerUserId(saved.getOwnerUser().getUserSeq())
                .sitterUserId(saved.getSitterUser().getUserSeq())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .status(saved.getStatus().name())
                .build();
    }

    // 2. 돌봄 확정 (확정 시간 저장 및 DTO 반환 시 포함)
    @Override
    @Transactional
    public CareSessionDto confirmSession(Integer sessionId) {
        CareSession session = careSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        // ⭐ 상태 변경 및 확정 시간 기록
        session.setStatus(CareSessionStatus.CONFIRMED);
        session.setConfirmedDate(LocalDateTime.now()); 

        CareSession saved = careSessionRepository.save(session);

        // 확정 시간을 포함하여 DTO 반환
        return CareSessionDto.builder()
                .id(saved.getSessionSeq())
                .ownerUserId(saved.getOwnerUser().getUserSeq())
                .sitterUserId(saved.getSitterUser().getUserSeq())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .status(saved.getStatus().name())
                .confirmedDate(saved.getConfirmedDate()) // ⭐ DTO에 포함
                .build();
    }
    
    // 3. 세션 ID를 통해 최신 상태 조회 (ChatService에서 사용)
    @Override
    @Transactional(readOnly = true)
    public CareSessionDto getSessionById(Integer sessionId) {
        CareSession session = careSessionRepository.findById(sessionId)
                .orElse(null); 

        if (session == null) {
            return null; // 세션이 존재하지 않으면 null 반환
        }
        
        // 엔티티를 DTO로 변환하여 반환
        return CareSessionDto.builder()
                .id(session.getSessionSeq())
                .ownerUserId(session.getOwnerUser().getUserSeq())
                .sitterUserId(session.getSitterUser().getUserSeq())
                .startDate(session.getStartDate())
                .endDate(session.getEndDate())
                .status(session.getStatus().name())
                .confirmedDate(session.getConfirmedDate()) // ⭐ DTO에 포함
                .build();
    }
}