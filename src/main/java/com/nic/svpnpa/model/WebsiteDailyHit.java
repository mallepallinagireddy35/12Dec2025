package com.nic.svpnpa.model;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(
    name = "website_daily_hits",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"ip_address", "hit_date"}
        )
    }
)
public class WebsiteDailyHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(name = "hit_date", nullable = false)
    private LocalDate hitDate;

    @Column(name = "language", nullable = false, length = 10)
    private String language;

    public Long getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDate getHitDate() {
        return hitDate;
    }

    public void setHitDate(LocalDate hitDate) {
        this.hitDate = hitDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}



