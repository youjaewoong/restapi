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
	
	@Test
	public void testFree() {
		// Given1
		Event event = Event.builder()
				.basePrice(0)
				.maxPrice(0)
				.build();
		// When
		event.update();
		
		// Then
		assertThat(event.isFree()).isTrue();
		
		// Given2
		event = Event.builder()
				.basePrice(100)
				.maxPrice(0)
				.build();
		// When
		event.update();
		
		// Then
		assertThat(event.isFree()).isFalse();

		// Given3
		event = Event.builder()
				.basePrice(0)
				.maxPrice(100)
				.build();
		// When
		event.update();
		
		// Then
		assertThat(event.isFree()).isFalse();
	}
	
	@Test
	public void testOffline() {
		// Given1
		Event event = Event.builder()
				.location("강남역 네이버 D2 스타텁 팩토리")
				.build();
		// When
		event.update();
		
		// Then
		assertThat(event.isOffline()).isTrue();
		
		// Given2
		event = Event.builder()
				.build();
		// When
		event.update();
		
		// Then
		assertThat(event.isOffline()).isFalse();
	}

}
