package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constants.Qualifiers;
import ru.yandex.practicum.filmorate.dto.DirectoryDTO;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(@Qualifier(Qualifiers.MPA) MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public DirectoryDTO getMpaRating(Long mpaId) {
        DirectoryDTO directoryDTO = MpaMapper.mpaToDirectoryDTO(mpaDbStorage.getMpaRating(mpaId));
        return directoryDTO;
    }

    public Collection<DirectoryDTO> getMpaRatings() {
        Collection<DirectoryDTO> mpas = mpaDbStorage.getMpaRatings()
                .stream()
                .map(MpaMapper::mpaToDirectoryDTO)
                .collect(Collectors.toSet());
        return mpas;
    }

}
