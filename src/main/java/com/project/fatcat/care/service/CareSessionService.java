package com.project.fatcat.care.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.care.repository.CareSessionRepository;
import com.project.fatcat.entity.CareSession;

@Service
public class CareSessionService {
	
	private final CareSessionRepository careSessionRepository;
	
	public CareSessionService(CareSessionRepository careSessionRepository) {
		
		this.careSessionRepository = careSessionRepository;
	}
	
	public List<CareSessionDto> getAllSessions(){
		
		List<CareSession> sessions = careSessionRepository.findAllwithUsers();
		
		return sessions.stream()
				.map(CareSessionDto::new)
				.collect(Collectors.toList());
	}
		

}
