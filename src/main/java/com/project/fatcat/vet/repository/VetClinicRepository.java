package com.project.fatcat.vet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.VetClinic;

public interface VetClinicRepository extends JpaRepository<VetClinic, Integer> {

	Optional<VetClinic> findByClinicNameAndClinicAddress(String clinicName, String clinicAddress);
}
