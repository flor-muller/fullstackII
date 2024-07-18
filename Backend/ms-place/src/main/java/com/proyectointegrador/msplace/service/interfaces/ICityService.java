package com.proyectointegrador.msplace.service.interfaces;

import com.proyectointegrador.msplace.dto.CityDTO;
import com.proyectointegrador.msplace.dto.PlaceOnlyDTO;
import java.util.Optional;
import java.util.Set;

public interface ICityService {
    Optional<CityDTO> getCityById(Long id);
    Optional<CityDTO> getCityByName(String name);
    Set<CityDTO> getAllCities();
    Set<CityDTO> getCityByZipCode(String zipCode);
    CityDTO addCity(CityDTO cityDTO);
    CityDTO updateCity(CityDTO cityDTO);
    void deleteCityById(Long id);
    void deleteCityByName(String name);
    Set<PlaceOnlyDTO> getAllPlacesByCityId(Long id);
    Set<PlaceOnlyDTO> getAllPlacesByCityName(String name);
}
