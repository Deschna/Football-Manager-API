package com.example.footballmanager.model;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal playerTransferCommission;
    private BigDecimal budget;
}
