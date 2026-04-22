package com.cinelog.cinelog;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {

    private static final Set<String> ALLOWED_STATUSES =
            Set.of("want_to_watch", "watching", "watched", "dropped");

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
    }

    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    public List<Movie> getByGenre(Long genreId) {
        return movieRepository.findByGenreId(genreId);
    }

    public Movie getById(Long id) {
        return findMovieOrThrow(id);
    }

    @Transactional
    public Movie create(Movie movie) {
        validateMovieForWrite(movie);
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie update(Long id, Movie updated) {
        Movie existing = findMovieOrThrow(id);
        Integer effectiveRating = existing.getRating();

        if (updated.getStatus() != null) {
            validateStatus(updated.getStatus());
            validateTransition(existing.getStatus(), updated.getStatus());
            if ("watched".equals(updated.getStatus()) && updated.getRating() == null) {
                throw new IllegalArgumentException("Cannot set status to 'watched' without a rating.");
            }
            existing.setStatus(updated.getStatus());
        }

        if (updated.getTitle() != null) {
            existing.setTitle(updated.getTitle());
        }

        if (updated.getGenre() != null) {
            existing.setGenre(updated.getGenre());
        }

        if (updated.getRating() != null) {
            validateRating(updated.getRating());
            existing.setRating(updated.getRating());
            effectiveRating = updated.getRating();
        }

        if ("watched".equals(existing.getStatus()) && effectiveRating == null) {
            throw new IllegalArgumentException("Cannot set status to 'watched' without a rating.");
        }

        existing.setWatchedAt(updated.getWatchedAt());

        return movieRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Movie existing = findMovieOrThrow(id);
        movieRepository.delete(existing);
    }

    @Transactional
    public Movie updateStatus(Long id, String newStatus) {
        Movie movie = findMovieOrThrow(id);

        validateStatus(newStatus);
        validateTransition(movie.getStatus(), newStatus);

        if ("watched".equals(newStatus) && movie.getRating() == null) {
            throw new IllegalArgumentException("Cannot set status to 'watched' without a rating.");
        }

        movie.setStatus(newStatus);
        return movieRepository.save(movie);
    }

    private Movie findMovieOrThrow(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + id));
    }

    private void validateMovieForWrite(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie payload cannot be null.");
        }

        validateStatus(movie.getStatus());
        validateRating(movie.getRating());

        if ("watched".equals(movie.getStatus()) && movie.getRating() == null) {
            throw new IllegalArgumentException("Cannot set status to 'watched' without a rating.");
        }
    }

    private void validateStatus(String status) {
        if (!ALLOWED_STATUSES.contains(status)) {
            throw new IllegalArgumentException(
                    "Invalid status: " + status + ". Allowed values are want_to_watch, watching, watched, dropped.");
        }
    }

    private void validateRating(Integer rating) {
        if (rating != null && (rating < 1 || rating > 10)) {
            throw new IllegalArgumentException("Rating must be between 1 and 10 when provided.");
        }
    }

    private void validateTransition(String currentStatus, String newStatus) {
        if (currentStatus.equals(newStatus)) {
            return;
        }

        boolean validTransition =
                ("want_to_watch".equals(currentStatus) &&
                        ("watching".equals(newStatus) || "dropped".equals(newStatus)))
                        || ("watching".equals(currentStatus) &&
                        ("watched".equals(newStatus) || "dropped".equals(newStatus)));

        if (!validTransition) {
            throw new IllegalArgumentException(
                    "Invalid status transition from '" + currentStatus + "' to '" + newStatus +
                            "'. Allowed transitions are: want_to_watch->watching, want_to_watch->dropped, watching->watched, watching->dropped.");
        }
    }
}
