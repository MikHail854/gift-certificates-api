package ru.clevertec.ecl.entty;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotBlank
    @Pattern(regexp = "^[-a-zA-Zа-яА-Я0-9 ]*$")
    @Size(min = 2, max = 25, message = "Name should be between 2 and 25 characters")
    @Column(name = "name")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[-a-zA-Zа-яА-Я0-9 ]*$")
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
            name = "gift_certificate_tag"
            , joinColumns = @JoinColumn(name = "gift_certificate_id")
            , inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

}

