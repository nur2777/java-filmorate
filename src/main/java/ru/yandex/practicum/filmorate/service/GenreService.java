package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constants.Qualifiers;
import ru.yandex.practicum.filmorate.dto.DirectoryDTO;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(@Qualifier(Qualifiers.GENRE) GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public DirectoryDTO getGenre(Long genreId) {
        DirectoryDTO directoryDTO = GenreMapper.genreToDirectoryDTO(genreDbStorage.getGenre(genreId));
        return directoryDTO;
    }

    public Collection<DirectoryDTO> getGenres() {
        Collection<DirectoryDTO> genres = genreDbStorage.getGenres()
                .stream()
                .map(GenreMapper::genreToDirectoryDTO)
                .collect(Collectors.toSet());
        return genres;
    }

}
