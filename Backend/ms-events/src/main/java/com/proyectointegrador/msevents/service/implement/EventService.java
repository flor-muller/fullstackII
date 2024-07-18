package com.proyectointegrador.msevents.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectointegrador.msevents.domain.*;
import com.proyectointegrador.msevents.dto.CategoryDTO;
import com.proyectointegrador.msevents.dto.DateEventDTO;
import com.proyectointegrador.msevents.dto.EventDTO;
import com.proyectointegrador.msevents.dto.EventGetDTO;
import com.proyectointegrador.msevents.exceptions.ResourceNotFoundException;
import com.proyectointegrador.msevents.repository.*;
import com.proyectointegrador.msevents.service.interfaces.ICategoryService;
import com.proyectointegrador.msevents.service.interfaces.IDateEventService;
import com.proyectointegrador.msevents.service.interfaces.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private final IEventRepository eventRepository;

    private final IDateEventRepository dateEventRepository;

    private final PlaceRepository placeRepository;

    private final ICategoryService categoryService;

    private final IDateEventService dateEventService;

    private final IImagesRepository imagesRepository;

    private final AwsService awsService;

    private final ObjectMapper mapper;

    private final ICategoryRepository categoryRepository;

    @Transactional
    protected EventDTO saveEvent(EventDTO eventDTO, MultipartFile file) throws Exception {
        DateEvent dateEvent = eventDTO.getDateEvent();
        if (dateEvent == null) {
            throw new IllegalArgumentException("DateEvent is required");
        }

        dateEventRepository.save(dateEvent);

        String imageUrl = awsService.uploadFile(file);
        Images images = new Images();
        images.setUrl(imageUrl);
        imagesRepository.save(images);
        eventDTO.setImages(images);

        Category category = eventDTO.getCategory();
        if (category == null || category.getId() == null) {
            throw new IllegalArgumentException("Category is required and must have an ID");
        }

        Optional<Category> optionalCategory = categoryRepository.findById(category.getId());
        if (optionalCategory.isEmpty()) {
            throw new IllegalArgumentException("Category with ID " + category.getId() + " not found");
        }

        category = optionalCategory.get();

        Event event = mapper.convertValue(eventDTO, Event.class);
        event.setDateEvent(dateEvent);
        event.setCategory(category);
        eventRepository.save(event);

        return mapper.convertValue(event, EventDTO.class);
    }

    @Override
    public Optional<EventGetDTO> getEventById(Long id) {
        Optional<Event> event = eventRepository.findEventById(id);
        EventGetDTO eventDTO;
        if (event.isPresent()) {
            eventDTO = mapper.convertValue(event, EventGetDTO.class);
            Optional<Place> place = placeRepository.getPlaceById(id);
            place.ifPresent(eventDTO::setPlace);
            return Optional.ofNullable(eventDTO);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<EventGetDTO> getEventByName(String name) {
        Optional<Event> event = eventRepository.findEventByName(name);
        EventGetDTO eventDTO;
        if (event.isPresent()) {
            eventDTO = mapper.convertValue(event, EventGetDTO.class);
            Optional<Place> place = placeRepository.getPlaceById(event.get().getPlaceId());
            place.ifPresent(eventDTO::setPlace);
            return Optional.ofNullable(eventDTO);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Set<EventGetDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        Set<Long> placeIds = events.stream()
                .map(Event::getPlaceId)
                .collect(Collectors.toSet());
        List<Place> places = placeRepository.getPlacesByIds(new ArrayList<>(placeIds));
        Map<Long, Place> placeMap = places.stream()
                .collect(Collectors.toMap(Place::getId, Function.identity()));
        Set<EventGetDTO> eventsDTO = new HashSet<>();
        for (Event event : events) {
            EventGetDTO eventGetDTO = mapper.convertValue(event, EventGetDTO.class);
            eventGetDTO.setPlace(placeMap.get(event.getPlaceId()));
            eventsDTO.add(eventGetDTO);
        }
        return eventsDTO;
    }

    @Override
    public List<EventGetDTO> searchEvents(String name, String category, String city, Date date) {
        Specification<Event> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            Optional<Event> eventName = eventRepository.findEventByName(name);
            if (eventName.isPresent()) {
                Event event = eventName.get();
                spec = spec.and((root, query, cb) -> cb.equal(root.get("id"), event.getId()));
            } else {
                return Collections.emptyList();
            }
        }

        if (category != null && !category.isEmpty()) {
            Optional<CategoryDTO> categoryOptional = categoryService.getCategoryByName(category);
            if (categoryOptional.isPresent()) {
                Long categoryId = categoryOptional.get().getId();
                List<Event> eventCategory = eventRepository.findByCategory(categoryId);
                if (!eventCategory.isEmpty()) {
                    List<Long> eventIds = eventCategory.stream().map(Event::getId).collect(Collectors.toList());
                    spec = spec.and((root, query, cb) -> root.get("id").in(eventIds));
                } else {
                    return Collections.emptyList();
                }
            } else {
                return Collections.emptyList();
            }
        }

        if (city != null && !city.isEmpty()) {
            Set<Place> places = placeRepository.getPlaceByCity(city);
            if (!places.isEmpty()) {
                List<Long> placesIds = places.stream().map(Place::getId).collect(Collectors.toList());
                List<Event> eventCity = new ArrayList<>();
                for (Long id : placesIds) {
                    List<Event> events = eventRepository.findByPlaceId(id);
                    if (events != null && !events.isEmpty()) {
                        eventCity.addAll(events);
                    }
                }
                if (!eventCity.isEmpty()) {
                    List<Long> eventIds = eventCity.stream().map(Event::getId).collect(Collectors.toList());
                    spec = spec.and((root, query, cb) -> root.get("id").in(eventIds));
                } else {
                    return Collections.emptyList();
                }
            } else {
                return Collections.emptyList(); // Si no se encuentran lugares por ciudad, devolver lista vacía
            }
        }

        if (date != null) {
            Optional<DateEventDTO> dateEventOptional = dateEventService.getDateEventByDate(date);
            if (dateEventOptional.isPresent()) {
                Long eventDateId = dateEventOptional.get().getId();
                List<Event> eventDate = eventRepository.findEventByDateEvent(eventDateId);
                if (!eventDate.isEmpty()) {
                    List<Long> eventIds = eventDate.stream().map(Event::getId).collect(Collectors.toList());
                    spec = spec.and((root, query, cb) -> root.get("id").in(eventIds));
                } else {
                    return Collections.emptyList();
                }
            } else {
                return Collections.emptyList();
            }
        }

        List<Event> events = eventRepository.findAll(spec);

        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> placeIds = events.stream()
                .map(Event::getPlaceId)
                .collect(Collectors.toSet());

        List<Place> places = placeRepository.getPlacesByIds(new ArrayList<>(placeIds));

        Map<Long, Place> placeMap = places.stream()
                .collect(Collectors.toMap(Place::getId, Function.identity()));

        List<EventGetDTO> eventsDTO = new ArrayList<>();
        for (Event event : events) {
            EventGetDTO eventGetDTO = mapper.convertValue(event, EventGetDTO.class);
            eventGetDTO.setPlace(placeMap.get(event.getPlaceId()));
            eventsDTO.add(eventGetDTO);
        }

        return eventsDTO;
    }
    @Override
    public List<Long> searchEventsReport(Map<String, String> criteria) {
        Specification<Event> spec = Specification.where(null);

        String name = criteria.get("eventName");
        String category = criteria.get("category");
        String city = criteria.get("city");
        String place = criteria.get("place");


        if (name != null && !name.isEmpty()) {
            Optional<Event> eventName = eventRepository.findEventByName(name);
            if (eventName.isPresent()) {
                Event event = eventName.get();
                spec = spec.and((root, query, cb) -> cb.equal(root.get("id"), event.getId()));
            } else {
                return Collections.emptyList();
            }
        }

        if (category != null && !category.isEmpty()) {
            Optional<CategoryDTO> categoryOptional = categoryService.getCategoryByName(category);
            if (categoryOptional.isPresent()) {
                Long categoryId = categoryOptional.get().getId();
                List<Event> eventCategory = eventRepository.findByCategory(categoryId);
                if (!eventCategory.isEmpty()) {
                    List<Long> eventIds = eventCategory.stream().map(Event::getId).collect(Collectors.toList());
                    spec = spec.and((root, query, cb) -> root.get("id").in(eventIds));
                } else {
                    return Collections.emptyList();
                }
            } else {
                return Collections.emptyList();
            }
        }

        if (city != null && !city.isEmpty()) {
            Set<Place> places = placeRepository.getPlaceByCity(city);
            if (!places.isEmpty()) {
                List<Long> placesIds = places.stream().map(Place::getId).collect(Collectors.toList());
                List<Event> eventCity = new ArrayList<>();
                for (Long id : placesIds) {
                    List<Event> events = eventRepository.findByPlaceId(id);
                    if (events != null && !events.isEmpty()) {
                        eventCity.addAll(events);
                    }
                }
                if (!eventCity.isEmpty()) {
                    List<Long> eventIds = eventCity.stream().map(Event::getId).collect(Collectors.toList());
                    spec = spec.and((root, query, cb) -> root.get("id").in(eventIds));
                } else {
                    return Collections.emptyList();
                }
            } else {
                return Collections.emptyList();
            }
        }

        if (place != null && !place.isEmpty()) {
            Optional<Place> placeOptional = placeRepository.getPlaceByName(place);
            if (placeOptional.isPresent()) {
                Long placeId = placeOptional.get().getId();
                List<Event> eventsPlace = eventRepository.findByPlaceId(placeId);
                if (eventsPlace != null && !eventsPlace.isEmpty()) {
                    List<Long> eventIds = eventsPlace.stream().map(Event::getId).collect(Collectors.toList());
                    spec = spec.and((root, query, cb) -> root.get("id").in(eventIds));
                } else {
                    return Collections.emptyList();
                }
            } else {
                return Collections.emptyList();
            }
        }

        List<Event> events = eventRepository.findAll(spec);
        return events.stream().map(Event::getId).collect(Collectors.toList());
    }

    @Override
    public EventDTO addEvent(EventDTO eventDTO, MultipartFile file) throws Exception {
        return saveEvent(eventDTO, file);
    }

    @Override
    public EventDTO updateEvent(EventDTO eventDTO) throws ResourceNotFoundException {
        Optional<Event> optionalEvent = eventRepository.findEventById(eventDTO.getId());
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setName(eventDTO.getName());
            event.setDescription(eventDTO.getDescription());
            event.setImages(eventDTO.getImages());
            event.setCategory(eventDTO.getCategory());
            event.setDateEvent(eventDTO.getDateEvent());
            event.setPlaceId(eventDTO.getPlaceId());
            Event eventUpdated = eventRepository.save(event);
            return mapper.convertValue(eventUpdated, EventDTO.class);
        }
        else {
            throw new ResourceNotFoundException("Event not found with id: " + eventDTO.getId());
        }
    }

    @Override
    public void deleteEventById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void deleteEventByName(String name) {
        Optional<Event> event = eventRepository.findEventByName(name);
        event.ifPresent(eventRepository::delete);
    }

    @Override
    public List<Event> findByPlaceId(Long id) {
        return eventRepository.findByPlaceId(id);
    }
}
