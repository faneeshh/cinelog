package com.cinelog.cinelog;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final GenreRepository genreRepository;

    public MovieController(MovieService movieService, GenreRepository genreRepository) {
        this.movieService = movieService;
        this.genreRepository = genreRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("movies", movieService.getAll());
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("newMovie", new Movie());
        return "movies/index";
    }

    @GetMapping("/fragment/list")
    public String fragmentList(@RequestParam(required = false) Long genreId, Model model) {
        List<Movie> movies = (genreId != null)
                ? movieService.getByGenre(genreId)
                : movieService.getAll();
        model.addAttribute("movies", movies);
        model.addAttribute("genres", genreRepository.findAll());
        return "fragments/movie :: movieList";
    }

    @PostMapping
    public String create(@ModelAttribute Movie movie,
                         @RequestParam Long genreId,
                         Model model) {
        try {
            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new IllegalArgumentException("Genre not found with id: " + genreId));
            movie.setGenre(genre);
            movieService.create(movie);
            model.addAttribute("movies", movieService.getAll());
            model.addAttribute("genres", genreRepository.findAll());
            return "fragments/movie :: movieList";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "fragments/movie :: formError";
        }
    }

    @PutMapping("/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               Model model) {
        try {
            movieService.updateStatus(id, status);
            model.addAttribute("movies", movieService.getAll());
            model.addAttribute("genres", genreRepository.findAll());
            return "fragments/movie :: movieList";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "fragments/movie :: formError";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Model model) {
        movieService.delete(id);
        model.addAttribute("movies", movieService.getAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "fragments/movie :: movieList";
    }
}
