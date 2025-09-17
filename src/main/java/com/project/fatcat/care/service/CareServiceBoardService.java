package com.project.fatcat.care.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.fatcat.care.repository.CareServiceBoardRepository;
import com.project.fatcat.entity.CareServiceBoard;

@Service
public class CareServiceBoardService {

	@Autowired
	private CareServiceBoardRepository careServiceboardRepository;
	
	public CareServiceBoard registerCareBoard(CareServiceBoard careServiceBoard) {
		return this.careServiceboardRepository.save(careServiceBoard);
	}
	
	public List<CareServiceBoard> getCareBoardsByRegion(String sido, String sigungu, String dong){
		
		String address1 = sido + " " + sigungu;
		
		if(dong != null && !dong.isEmpty()) {
			return careServiceboardRepository.findByAddress1AndAddress2(address1, dong);
		}else {
			return careServiceboardRepository.findByAddress1(address1);
		}
	}
}
