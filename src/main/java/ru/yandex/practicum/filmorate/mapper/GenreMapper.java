package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.DirectoryDTO;
import ru.yandex.practicum.filmorate.model.Genre;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenreMapper {

    public static DirectoryDTO genreToDirectoryDTO(Genre genre) {
        DirectoryDTO directoryDTO = DirectoryDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
        return directoryDTO;
    }
}