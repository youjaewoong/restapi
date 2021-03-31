package com.example.demo.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.common.ErrorsResource;


@Controller
@RequestMapping(value= "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	private final EventValidator eventValidator;
	
	public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
		this.eventValidator = eventValidator;
		this.eventRepository = eventRepository;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping
	public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
		
		if(errors.hasErrors()) {
			//return ResponseEntity.badRequest().build();
			return badRequest(errors);
		}
		
		eventValidator.validate(eventDto, errors);
		if(errors.hasErrors()) {
			//return ResponseEntity.badRequest().build();
			return badRequest(errors);
		}
		
		//dto class 를  Event의 객체로 변경요청
		Event event = modelMapper.map(eventDto, Event.class);
		event.update();
		Event newEvent = this.eventRepository.save(event);
		
		WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
		URI createdUri = selfLinkBuilder.toUri();
		
		EventResource eventResource = new EventResource(event);
		eventResource.add(linkTo(EventController.class).withRel("query-events"));
		eventResource.add(selfLinkBuilder.withRel("update-event"));
		eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
		
		return ResponseEntity.created(createdUri).body(eventResource);
	}
	
	@GetMapping
	public ResponseEntity<?> queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){
		Page<Event> page = this.eventRepository.findAll(pageable);
		var pagedResources = assembler.toModel(page, e -> new EventResource(e));
		pagedResources.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
		return ResponseEntity.ok(pagedResources);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getEvent(@PathVariable Integer id){
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		if (optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Event event = optionalEvent.get();
		EventResource eventResource = new EventResource(event);
		eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
		return ResponseEntity.ok(eventResource);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity updateEvent(	@PathVariable Integer id,
										@RequestBody @Valid EventDto eventDto,
										Errors errors) {
		
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		
		if(optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		this.eventValidator.validate(eventDto, errors);
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		Event existingEvent = optionalEvent.get();
		this.modelMapper.map(eventDto, existingEvent);
		Event saveEvent = this.eventRepository.save(existingEvent);
		
		EventResource eventResource = new EventResource(saveEvent);
		eventResource.add(new Link("/docs/index.html#resources-events-update").withRel("profile"));
		
		return ResponseEntity.ok(eventResource);
	}	
	
	private ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}
	
 
}
