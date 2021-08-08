package com.test.pamc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.test.pamc.entity.EpaperEntity;


public interface EpaperRepository extends JpaRepository<EpaperEntity, Long> {
    Page<EpaperEntity> findAllByNewspaperName(String newspaperName, Pageable pageable);
}
