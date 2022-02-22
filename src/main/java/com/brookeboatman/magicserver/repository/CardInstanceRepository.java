package com.brookeboatman.magicserver.repository;

import com.brookeboatman.magicserver.domain.CardInstance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CardInstance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardInstanceRepository extends JpaRepository<CardInstance, Long> {}
