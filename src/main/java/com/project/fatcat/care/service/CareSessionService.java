package com.project.fatcat.care.service;

import com.project.fatcat.care.dto.CareSessionDto;

import java.util.List;

public interface CareSessionService {
    List<CareSessionDto> getAllSessions();
}