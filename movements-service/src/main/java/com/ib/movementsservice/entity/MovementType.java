package com.ib.movementsservice.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name= "movement_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MovementType implements Serializable{
    @Serial
    private static final long serialVersionUID= 1L;
    @Id
    private Long id;
    @NotNull
    @NotEmpty
    @Size(max = 45)
    private String description;

}
