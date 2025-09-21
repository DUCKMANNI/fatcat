package com.project.fatcat.vet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VetViewController {
	
	@GetMapping("/vetclinic")
	public String vet() {
		return "vet/vetclinic_map";
		}

}
