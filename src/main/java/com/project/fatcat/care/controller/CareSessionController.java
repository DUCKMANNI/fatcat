package com.project.fatcat.care.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.care.service.CareSessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/care")
public class CareSessionController {

    private final CareSessionService careSessionService;

    // 돌봄 확정 API
    @PutMapping("/confirm/{sessionId}")
    public ResponseEntity<CareSessionDto> confirmCare(@PathVariable Integer sessionId) {
        CareSessionDto confirmed = careSessionService.confirmSession(sessionId);
        return ResponseEntity.ok(confirmed);
    }
}
