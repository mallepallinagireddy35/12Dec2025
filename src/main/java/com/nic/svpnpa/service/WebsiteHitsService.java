    package com.nic.svpnpa.service;
	import com.nic.svpnpa.model.WebsiteDailyHit;
import com.nic.svpnpa.model.WebsiteHits;
import com.nic.svpnpa.dao.WebsiteDailyHitRepository;
import com.nic.svpnpa.dao.WebsiteHitsRepository;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;

	@Service
	public class WebsiteHitsService {

//	    private final WebsiteHitsRepository repository;
//
//	    public WebsiteHitsService(WebsiteHitsRepository repository) {
//	        this.repository = repository;
//	    }
//
//	    @Transactional
//	    public void incrementEnglishHits() {
//	        repository.incrementEnglishHits();
//	    }
//
//	    @Transactional
//	    public void incrementHindiHits() {
//	        repository.incrementHindiHits();
//	    }
//
//	    public WebsiteHits getHits() {
//	        return repository.findById(1L).orElseThrow(() ->
//	           new RuntimeException("Hit counter not found"));
//	    }
		
		
		private final WebsiteHitsRepository hitsRepository;
	    private final WebsiteDailyHitRepository dailyHitRepository;

	    public WebsiteHitsService(
	            WebsiteHitsRepository hitsRepository,
	            WebsiteDailyHitRepository dailyHitRepository) {
	        this.hitsRepository = hitsRepository;
	        this.dailyHitRepository = dailyHitRepository;
	    }

	    @Transactional
	    public void recordHit(String ipAddress, String language) {

	        LocalDate today = LocalDate.now();

	        // Already counted today
	        if (dailyHitRepository.existsByIpAddressAndHitDate(ipAddress, today)) {
	            return;
	        }

	        // Record today's hit
	        WebsiteDailyHit dailyHit = new WebsiteDailyHit();
	        dailyHit.setIpAddress(ipAddress);
	        dailyHit.setHitDate(today);
	        dailyHit.setLanguage(language);

	        dailyHitRepository.save(dailyHit);

	        // Increment global counter
	        if ("E".equalsIgnoreCase(language)) {
	            hitsRepository.incrementEnglishHits();
	        } else if ("H".equalsIgnoreCase(language)) {
	            hitsRepository.incrementHindiHits();
	        }
	    }

	    public WebsiteHits getHits() {
	        return hitsRepository.findById(1L)
	                .orElseThrow(() ->
	                    new RuntimeException("Hit counter not found"));
	    }

	}
