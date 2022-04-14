package ru.clevertec.ecl.entty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;


    /**
     * Список подарочных сертификатов тега {@link GiftCertificate}.
     */
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "tags")
    private List<GiftCertificate> giftCertificates;

}
