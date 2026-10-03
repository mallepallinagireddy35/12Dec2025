package com.nic.svpnpa.model;
import jakarta.persistence.*;

	@Entity
	@Table(name = "website_hits")
	public class WebsiteHits {

	    @Id
	    private Long id;

	    @Column(name = "english_hits")
	    private Long englishHits;

	    @Column(name = "hindi_hits")
	    private Long hindiHits;

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public Long getEnglishHits() {
	        return englishHits;
	    }

	    public void setEnglishHits(Long englishHits) {
	        this.englishHits = englishHits;
	    }

	    public Long getHindiHits() {
	        return hindiHits;
	    }

	    public void setHindiHits(Long hindiHits) {
	        this.hindiHits = hindiHits;
	    }
	}

