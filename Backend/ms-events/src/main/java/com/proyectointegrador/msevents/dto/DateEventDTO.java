package com.proyectointegrador.msevents.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(description = "Detalles del DateEventDTO")
public class DateEventDTO {
    @Schema(description = "ID del DateEvent", example = "1")
    private Long id;

    @Schema(description = "Date del DateEvent", example = "2024-06-04T14:32:00Z")
    private Date date;
}
