package com.cinelog.cinelog;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final MovieService movieService;
    private final MovieRepository movieRepository;

    public DashboardController(MovieService movieService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Movie> all = movieService.getAll();
        model.addAttribute("totalMovies", all.size());
        model.addAttribute("watchedCount", movieRepository.countByStatus("watched"));
        model.addAttribute("wantToWatchCount", movieRepository.countByStatus("want_to_watch"));
        model.addAttribute("moviesPerGenre", movieRepository.countByGenre());
        return "dashboard/index";
    }
}
