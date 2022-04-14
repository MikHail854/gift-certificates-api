package ru.clevertec.ecl.entty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
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

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "last_update_date")
    private Date lastUpdateDate;


    /**
     * Список тегов подарочного сертииката {@link Tag}.
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "gift_certificate_tags"
            , joinColumns = @JoinColumn(name = "id_gift_certificate")
            , inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    private List<Tag> tags;

}

