package com.example.demo.events;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EventTest {

	@Test
	void builder() {
		Event event = Event.builder()
				.name("spring rest api")
				.description("REST API development with Spring")
				.build();
		assertThat(event).isNotNull();
	}
	
	@Test
	void javaBean() {
		
		//Given
		String name = "Event";
		String description = "Spring";
		
		//When
		Event event = new Event();
		event.setName(name);
		event.setDescription(description);
		
		//Then
		assertThat(event.getName()).isEqualTo(name);
		assertThat(event.getDescription()).isEqualTo(description);
	}

}
