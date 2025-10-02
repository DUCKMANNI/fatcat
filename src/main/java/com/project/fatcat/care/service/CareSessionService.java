package com.project.fatcat.care.service;

import java.util.List;

import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.entity.CareSession;
import com.project.fatcat.entity.User;

public interface CareSessionService {
	
    CareSessionDto createSession(CareSessionDto dto);

	CareSessionDto confirmSession(Integer sessionId);

	CareSessionDto getSessionById(Integer sessionId);
	
	// 내가 맡긴 예약들
	List<CareSession> getOwnerSessions(User owner);
	
	// 내가 맡아주는 예약들
	List<CareSession> getSitterSessions(User sitter);
}