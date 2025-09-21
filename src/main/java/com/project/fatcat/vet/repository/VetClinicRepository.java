package com.project.fatcat.vet.repository;

import com.project.fatcat.entity.VetClinic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VetClinicRepository extends JpaRepository<VetClinic, Integer> {
    
    Optional<VetClinic> findByClinicNameAndClinicAddress(String clinicName, String clinicAddress);
    
}