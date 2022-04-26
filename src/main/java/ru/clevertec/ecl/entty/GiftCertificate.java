package ru.clevertec.ecl.entty;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Positive
    @Column(name = "price")
    private Float price;

    @NotNull
    @Positive
    @Column(name = "duration")
    private Integer duration;

    @NotNull
    @EqualsAndHashCode.Exclude
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @NotNull
    @EqualsAndHashCode.Exclude
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;


    /**
     * Список тегов подарочного сертииката {@link Tag}.
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "gift_certificate_tag"
            , joinColumns = @JoinColumn(name = "gift_certificate_id")
            , inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

}

