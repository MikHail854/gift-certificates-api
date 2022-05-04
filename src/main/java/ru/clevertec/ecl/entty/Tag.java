package ru.clevertec.ecl.entty;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
    private Integer id;

    @NotBlank
    @Pattern(regexp = "^[-a-zA-Zа-яА-Я0-9]+(\\s+[-a-zA-Zа-яА-Я0-9])*$")
    @Column(name = "name", unique = true)
    private String name;


    /**
     * Список подарочных сертификатов тега {@link GiftCertificate}.
     */
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "tags", fetch = FetchType.EAGER)
    private List<GiftCertificate> giftCertificates;

}
