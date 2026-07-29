package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.DirectoryDTO;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;


@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {

    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/{id}")
    public DirectoryDTO getMpaRating(@Valid @PathVariable Long id) {
        return mpaService.getMpaRating(id);
    }

    @GetMapping
    public Collection<DirectoryDTO> getAllRatings() {
        return mpaService.getMpaRatings();
    }
}
