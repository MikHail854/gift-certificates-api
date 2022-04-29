package ru.clevertec.ecl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entty.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByNameIgnoreCase(String name);

    @Query(value = "SELECT t.* FROM tag t " +
            "where t.id = ( " +
            "SELECT gct.tag_id FROM gift_certificate_tag gct " +
            "WHERE gct.gift_certificate_id in (" +
            "SELECT od.certificate_id FROM order_data od " +
            "WHERE od.user_id = (" +
            "SELECT od.user_id FROM order_data od GROUP BY od.user_id " +
            "HAVING sum(od.price) >= ALL ( " +
            "SELECT sum(od.price) FROM order_data od GROUP BY od.user_id)))" +
            "GROUP BY gct.tag_id ORDER BY count(gct.tag_id) DESC LIMIT 1);", nativeQuery = true)
    Optional<Tag> findTheMostWidelyUsedTag();
}
