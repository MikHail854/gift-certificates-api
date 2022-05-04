package ru.clevertec.ecl.entty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_data")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Positive
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @Positive
    @Column(name = "certificate_id")
    private Integer certificateId;

    @NotNull
    @Positive
    @Column(name = "price")
    private Float price;

    @NotNull
    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

}
