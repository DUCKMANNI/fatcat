package com.project.fatcat.care.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.care.repository.CareSessionRepository;
import com.project.fatcat.entity.CareSession;
import com.project.fatcat.entity.User;
import com.project.fatcat.entity.enums.CareSessionStatus;
import com.project.fatcat.users.UserRepository;

@Service
@RequiredArgsConstructor
public class CareSessionServiceImpl implements CareSessionService {

    private final CareSessionRepository careSessionRepository;
    private final UserRepository userRepository;

    // 돌봄 요청 생성
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

    // 돌봄 확정
    @Override
    @Transactional
    public CareSessionDto confirmSession(Integer sessionId) {
        CareSession session = careSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        session.setStatus(CareSessionStatus.CONFIRMED);
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
}
