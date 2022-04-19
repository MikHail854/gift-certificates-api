package ru.clevertec.ecl.entty;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gift_certificate")
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private float price;

    @Column(name = "duration")
    private int duration;

    @EqualsAndHashCode.Exclude
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @EqualsAndHashCode.Exclude
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;


    /**
     * Список тегов подарочного сертииката {@link Tag}.
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "gift_certificate_tags"
            , joinColumns = @JoinColumn(name = "id_gift_certificate")
            , inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    private List<Tag> tags;

}

