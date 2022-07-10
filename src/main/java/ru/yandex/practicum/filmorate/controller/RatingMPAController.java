package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.servis.RatingMPAService;

@RestController
@RequestMapping("/mpa")
public class RatingMPAController {
    private RatingMPAService ratingMPAService;

    @Autowired
    public RatingMPAController(RatingMPAService ratingMPAService) {
        this.ratingMPAService = ratingMPAService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRatingMPA() {
        return ResponseEntity.ok(ratingMPAService.getAllRatingMPA());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getRatingMPAById(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(ratingMPAService.getRatingMPAById(id));
    }
}
