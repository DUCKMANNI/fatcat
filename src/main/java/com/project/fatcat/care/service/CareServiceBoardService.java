package com.project.fatcat.care.service;

import java.util.List;

import com.project.fatcat.care.dto.CareServiceBoardDto;
import com.project.fatcat.care.dto.CareServiceBoardListDto;
import com.project.fatcat.entity.CareServiceBoard;

public interface CareServiceBoardService {
	
    void save(CareServiceBoardDto careServiceBoardDto);

   
    List<CareServiceBoardListDto> getAllBoardsAsDto();
    
    List<CareServiceBoardListDto> getBoardsByRegionAsDto(String region);
    
    List<CareServiceBoardListDto> getBoardsWithinRadiusAsDto(Double latitude, Double longitude, double radiusInKm);

    	void deleteBoard(Integer careSeq,Integer userSeq);
    	
    	CareServiceBoard getBoard(Integer careSeq);
    	
    void modify(Integer careSeq, CareServiceBoardDto careServiceBoardDto,  Integer userSeq);



	


}