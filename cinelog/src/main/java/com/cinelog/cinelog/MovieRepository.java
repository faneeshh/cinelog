package com.cinelog.cinelog;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByGenreId(Long genreId);

    long countByStatus(String status);

    @Query("SELECT m.genre.name, COUNT(m) FROM Movie m GROUP BY m.genre.name")
    List<Object[]> countByGenre();
}
