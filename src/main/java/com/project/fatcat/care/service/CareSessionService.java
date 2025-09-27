package com.project.fatcat.care.service;

import com.project.fatcat.care.dto.CareSessionDto;

public interface CareSessionService {
	
    CareSessionDto createSession(CareSessionDto dto);

	CareSessionDto confirmSession(Integer sessionId);

	CareSessionDto getSessionById(Integer sessionId);
}