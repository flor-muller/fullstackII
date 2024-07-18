package com.proyectointegrador.msevents.dto;
import com.proyectointegrador.msevents.domain.Category;
import com.proyectointegrador.msevents.domain.DateEvent;
import com.proyectointegrador.msevents.domain.Images;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(description = "Detalles de EventDTO")
public class EventDTO {
    @Schema(description = "ID del Evento", example = "1")
    private Long id;

    @Schema(description = "Nombre del evento", example = "The Weeknd")
    private String name;

    @Schema(description = "Descripción del evento", example = "After Hours til Dawn Tour en River, The Weeknd presenta sus dos albumes en el Estadio Monumental")
    private String description;

    @Schema(description = "Url de la foto del evento", example = "http://www.foto.com")
    private Images images;

    @Schema(description = "Categoria del evento", example = "Música")
    private Category category;

    @Schema(description = "Fecha del evento", example = "2024-09-14T22:15:00Z")
    private DateEvent dateEvent;

    @Schema(description = "Id del estadio donde se lleva a cabo el evento", example = "1")
    private Long placeId;
}
