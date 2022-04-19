package ru.clevertec.ecl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entty.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findTagByName(String name);
}
