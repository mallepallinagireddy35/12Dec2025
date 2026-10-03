package com.nic.svpnpa.dao;

import com.nic.svpnpa.model.WebsiteDailyHit;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteDailyHitRepository
        extends JpaRepository<WebsiteDailyHit, Long> {

	boolean existsByIpAddressAndHitDate(String ipAddress, LocalDate hitDate );
}


