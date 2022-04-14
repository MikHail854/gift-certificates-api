package ru.clevertec.ecl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entty.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findTagByName(String name);
}
