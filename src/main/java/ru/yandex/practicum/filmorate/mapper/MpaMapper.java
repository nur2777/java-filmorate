package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.DirectoryDTO;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MpaMapper {

    public static DirectoryDTO mpaToDirectoryDTO(Mpa mpa) {
        DirectoryDTO directoryDTO = DirectoryDTO.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
        return directoryDTO;
    }
}