package ru.clevertec.ecl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.ecl.entty.GiftCertificate;

import java.util.List;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Integer> {

    GiftCertificate findByTagsName(String tags_name);

    @Query(value = "select * from gift_certificate " +
            "where description like %:description% ", nativeQuery = true)
    List<GiftCertificate> findAllByDescription(@Param("description") String description);

}
