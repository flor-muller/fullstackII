package com.proyectointegrador.msevents.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString

@Schema(description = "Detalles del Evento")
@Entity
@Table(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del Evento", example = "1")
    private Long id;

    @Schema(description = "Nombre del evento", example = "The Weeknd")
    private String name;

    @Schema(description = "Descripción del evento", example = "After Hours til Dawn Tour en River, The Weeknd presenta sus dos albumes en el Estadio Monumental")
    private String description;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "images_id", referencedColumnName = "id")
    private Images images;

    @Column(name ="place_id",nullable = false)
    @Schema(description = "Id del estadio donde se lleva a cabo el evento", example = "1")
    private Long placeId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Schema(description = "Categoria del evento", example = "Música")
    private Category category;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "dateEvent_id")
    @Schema(description = "Fecha del evento", example = "2024-09-14T22:15:00Z")
    private DateEvent dateEvent;
}
