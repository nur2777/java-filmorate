package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.Collection;

@Service
@Slf4j
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(@Qualifier("mpaDbStorage") MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Mpa getMpaRating(Long mpaId) {
        return mpaDbStorage.getMpaRating(mpaId);
    }

    public Collection<Mpa> getMpaRatings() {
        return mpaDbStorage.getMpaRatings();
    }

}
