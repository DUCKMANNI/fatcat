package com.project.fatcat.care.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.care.repository.CareSessionRepository;
import com.project.fatcat.entity.CareSession;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CareSessionServiceImpl implements CareSessionService {
    
    private final CareSessionRepository careSessionRepository;
    
    
    @Override
    @Transactional(readOnly=true)
    public List<CareSessionDto> getAllSessions() {
        List<CareSession> sessions = careSessionRepository.findAllwithUsers();
        
        return sessions.stream()
                .map(CareSessionDto::new)
                .collect(Collectors.toList());
    }
}