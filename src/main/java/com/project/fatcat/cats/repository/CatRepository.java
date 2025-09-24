package com.project.fatcat.cats.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.Cat;

public interface CatRepository extends JpaRepository<Cat, Integer> {

	List<Cat> findByUserUserSeq(Integer userSeq);

}
