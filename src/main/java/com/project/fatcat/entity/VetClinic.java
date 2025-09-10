package com.project.fatcat.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vet_clinics")
public class VetClinic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vetSeq;
	
	@Column(nullable = false)
	private String clinicName;
	
	private String clinicNumber;
	
	@Column(nullable = false)
	private String clinicCity;
	
	@Column( nullable = false)
	private String clinicTown;
	
	private String clinicAddress;
	
	@Column(nullable = false)
	private Double latitude;
	
	@Column(nullable = false)
	private Double longitude;
	
	private Boolean isCatRoom;
	
	private Boolean isEmergency;
	
	private Boolean is_24h;
	
	private Boolean hasParking;
	
	private String closedDay;
	
	private String openTime;
	
	private String closeTime;
	
	private String breakStart;
	
	private String breakEnd;
	
	private String clinicImageUrl;
	
	@Column(columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
	@Builder.Default
	@OneToMany(mappedBy = "vetClinic", cascade = CascadeType.ALL)
	private List<VetReview> vetReviewList = new ArrayList<>();
	
	@Builder.Default
	@OneToMany(mappedBy = "vetClinic", cascade = CascadeType.ALL)
	private List<VetRatingAvg> vetRatingAvgList = new ArrayList<>();

}