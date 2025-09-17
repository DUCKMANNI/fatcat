package com.project.fatcat.care.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.fatcat.entity.CareServiceBoard;

@Controller
public class CareController {
	
	@GetMapping("care")
	public String care() {
		return "care/care_map";
	}
	

	
	

}
