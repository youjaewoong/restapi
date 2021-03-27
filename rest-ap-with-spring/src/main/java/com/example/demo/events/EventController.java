package com.example.demo.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value= "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	
	public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
		this.eventRepository = eventRepository;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping
	public ResponseEntity<?> createEvent(@RequestBody EventDto eventDto) {
		
		//dto class 를  Event의 객체로 변경요청
		Event event = modelMapper.map(eventDto, Event.class);
		
		//URI createdUri = linkTo(methodOn(EventController.class).createEvent()).slash("{id}").toUri();
		Event newEvent = this.eventRepository.save(event);
		URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
		//return ResponseEntity.created(createdUri).build();
		event.setId(10);
		return ResponseEntity.created(createdUri).body(event);
	}
}
