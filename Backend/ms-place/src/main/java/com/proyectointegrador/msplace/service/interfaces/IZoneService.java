package com.proyectointegrador.msplace.service.interfaces;

import com.proyectointegrador.msplace.dto.ZoneDTO;
import com.proyectointegrador.msplace.dto.ZoneOnlyDTO;
import java.util.Optional;
import java.util.Set;

public interface IZoneService {
    Optional<ZoneDTO> getZoneById(Long id);
    Optional<ZoneDTO> getZoneByName(String name);
    Set<ZoneDTO> getAllZones();
    ZoneDTO addZone(ZoneDTO zoneDTO);
    ZoneDTO updateZone(ZoneDTO zoneDTO);
    void deleteZoneById(Long id);
    void deleteZoneByName(String name);
    Integer getAvailability(Long id);
    void putAvailability(Integer number, Long id);
    Set<ZoneOnlyDTO> getAllZonesByPlaceId(Long id);
}
