package com.project.fatcat.users.service;

import java.io.IOException;

import com.project.fatcat.entity.User;
import com.project.fatcat.users.dto.SignupDTO;

public interface UserService {

	User register(SignupDTO dto) throws IOException;
	
}
