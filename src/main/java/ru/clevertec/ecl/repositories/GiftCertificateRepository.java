package ru.clevertec.ecl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entty.GiftCertificate;

import java.util.List;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Integer> {

    List<GiftCertificate> findByTagsName(String tags_name);

}
