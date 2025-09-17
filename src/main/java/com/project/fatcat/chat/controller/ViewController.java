package com.project.fatcat.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
 
	@GetMapping("/chat")
	public String showChatForm() {
		return "chat/chat_form";
	}
}
