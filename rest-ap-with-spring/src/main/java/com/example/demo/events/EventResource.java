package com.example.demo.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource extends EntityModel<Event>{
	
	public EventResource(Event event, Link... links) {
		super(event, links);
		
		//add(new Link("http://localhost:8080/api/events/" + event.getId())); 와 같은것
		add(linkTo(EventController.class).slash(event.getId()).withSelfRel()); //withSelfRel() : 'self' 링크를 추가해주는 메소드
	}

}
