package com.project.fatcat.care.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.care.service.CareSessionService;

@Controller
public class CareSessionController {

	private final CareSessionService careSessionService;
	
	public CareSessionController(CareSessionService careSessionService) {
		this.careSessionService = careSessionService;
		
	}
	
	@GetMapping("/api/session")
	@ResponseBody
	public ResponseEntity<List<CareSessionDto>> getSession(){
		List<CareSessionDto> sessions = careSessionService.getAllSessions();
		return ResponseEntity.ok(sessions);
	}
	}
