package com.project.fatcat.care.service;

import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.care.repository.CareSessionRepository;
import com.project.fatcat.entity.CareSession;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareSessionServiceImpl implements CareSessionService {
    
    private final CareSessionRepository careSessionRepository;
    
    
    @Override
    public List<CareSessionDto> getAllSessions() {
        List<CareSession> sessions = careSessionRepository.findAllwithUsers();
        
        return sessions.stream()
                .map(CareSessionDto::new)
                .collect(Collectors.toList());
    }
}