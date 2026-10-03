package com.nic.svpnpa.dao;

	import com.nic.svpnpa.model.WebsiteHits;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Modifying;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.stereotype.Repository;

	@Repository
	public interface WebsiteHitsRepository
	        extends JpaRepository<WebsiteHits, Long> {

	    @Modifying
	    @Query("""
	           UPDATE WebsiteHits h
	           SET h.englishHits = h.englishHits + 1
	           WHERE h.id = 1
	           """)
	    int incrementEnglishHits();

	    @Modifying
	    @Query("""
	           UPDATE WebsiteHits h
	           SET h.hindiHits = h.hindiHits + 1
	           WHERE h.id = 1
	           """)
	    int incrementHindiHits();
	}
