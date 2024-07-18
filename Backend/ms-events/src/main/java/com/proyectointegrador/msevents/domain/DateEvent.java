package com.proyectointegrador.msevents.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@Entity
@Table(name = "DateEvent")
@Schema(description = "Detalles del DateEvent")
public class DateEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del DateEvent", example = "1")
    private Long id;

    @Schema(description = "Date del DateEvent", example = "2024-06-04T14:32:00Z")
    private Date date;
}
