package com.project.fatcat.vet.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class VetViewController {
	
	@GetMapping("/vetclinic")
	public String vet() {
		return "vet/vetclinic_map";
		}

}
