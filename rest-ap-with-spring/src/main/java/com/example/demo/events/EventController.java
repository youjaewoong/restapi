package com.example.demo.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value= "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

	@Autowired
	EventRepository eventRepository;
	
	@PostMapping
	public ResponseEntity<?> createEvent(@RequestBody Event event) {
		
		//URI createdUri = linkTo(methodOn(EventController.class).createEvent()).slash("{id}").toUri();
		Event newEvent = this.eventRepository.save(event);
		URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
		//return ResponseEntity.created(createdUri).build();
		event.setId(10);
		return ResponseEntity.created(createdUri).body(event);
	}
}
