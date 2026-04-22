package com.cinelog.cinelog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String status;

    @Min(1)
    @Max(10)
    @Column
    private Integer rating;

    @Column
    private LocalDate watchedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public Movie() {
    }

    public Movie(Long id, String title, String status, Integer rating,
                 LocalDate watchedAt, LocalDateTime createdAt, Genre genre) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.rating = rating;
        this.watchedAt = watchedAt;
        this.createdAt = createdAt;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDate getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(LocalDate watchedAt) {
        this.watchedAt = watchedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
