package com.example.demo.events;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.common.RestDocsConfiguration;
import com.example.demo.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
class EventControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Test
	@TestDescription("정상적으로 이벤트를 생성하는 테스트")
	void createEvent() throws Exception {
		EventDto eventDto = EventDto.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
				.closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
				.beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
				.endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 스타텁 팩토리")
				.build();
		
		mockMvc.perform(post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(eventDto)))
		.andDo(print())
		.andExpect(status().isCreated()) //201
		.andExpect(jsonPath("id").exists())
		.andExpect(header().exists(HttpHeaders.LOCATION))
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
		.andExpect(jsonPath("free").value(false))
		.andExpect(jsonPath("offline").value((true)))
		.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
		.andDo(document("create-event", 
				links(
						linkWithRel("self").description("link to self"),
						linkWithRel("query-events").description("link to events"),
						linkWithRel("update-event").description("link to update an existing event"),
						linkWithRel("profile").description("link to profile an existing event")
						
				),
				requestHeaders(
						headerWithName(HttpHeaders.ACCEPT).description("accept header"),
						headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
				),
				requestFields(
						fieldWithPath("name").description("Name of new event"),
						fieldWithPath("description").description("description of new event"),
						fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
						fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
						fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
						fieldWithPath("endEventDateTime").description("date time of end of new event"),
						fieldWithPath("location").description("location of new event"),
						fieldWithPath("basePrice").description("base price of new event"),
						fieldWithPath("maxPrice").description("max price of new event"),
						fieldWithPath("limitOfEnrollment").description("limit of enrolmment")
				),
				responseHeaders(
						headerWithName(HttpHeaders.LOCATION).description("Location header"),
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
				),
				responseFields(
						fieldWithPath("id").description("identifier of new event"),
						fieldWithPath("name").description("Name of new event"),
						fieldWithPath("description").description("description of new event"),
						fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
						fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
						fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
						fieldWithPath("endEventDateTime").description("date time of end of new event"),
						fieldWithPath("location").description("location of new event"),
						fieldWithPath("basePrice").description("base price of new event"),
						fieldWithPath("maxPrice").description("max price of new event"),
						fieldWithPath("limitOfEnrollment").description("limit of enrolmment"),
						fieldWithPath("free").description("it tells if this event is free or not"),
						fieldWithPath("offline").description("it tells if this event is offline meeting or not"),
						fieldWithPath("eventStatus").description("event status"),
						fieldWithPath("_links.self.href").description("link to self"),
						fieldWithPath("_links.query-events.href").description("link to query event list"),
						fieldWithPath("_links.update-event.href").description("link to update existing event"),
						fieldWithPath("_links.profile.href").description("link to profile an existing event")
				)
				
		))
		;
	}
	
	@Test
	@TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
	void createEvent_Bad_Request() throws Exception {
		Event event = Event.builder()
				.id(100)
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
				.closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
				.beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
				.endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 스타텁 팩토리")
				.free(true)
				.offline(false)
				.eventStatus(EventStatus.PUBLISHED)
				.build();
		
		mockMvc.perform(post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(event)))
				.andDo(print())
				.andExpect(status().isBadRequest()) //201
		;
	}
	
	@Test
	@TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
	void createEvent_Bad_Reuqest_Empoty_Input() throws Exception{
		EventDto eventDto = EventDto.builder().build();
		this.mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(eventDto)))
			.andExpect(status().isBadRequest())
		;
	}
	
	@Test
	@TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
	void createEvent_Bad_Reuqest_Wrong_Input() throws Exception{
		EventDto eventDto = EventDto.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
				.closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
				.beginEventDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
				.endEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
				.basePrice(10000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 스타텁 팩토리")
				.build();
		
		mockMvc.perform(post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(eventDto)))
		  	.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("content[0].objectName").exists())
			.andExpect(jsonPath("content[0].defaultMessage").exists())
			.andExpect(jsonPath("content[0].code").exists())
			.andExpect(jsonPath("_links.index").exists())
		;
	}
	
	@Test
	@TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
	public void queryEvents() throws Exception {
		// Given
		IntStream.range(0, 30).forEach(i -> {
			this.generateEvent(i);
		});
		
		// When
		this.mockMvc.perform(get("/api/events")
				.param("page", "1")
				.param("size", "10")
				.param("sort", "name,DESC")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("page").exists())
			.andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.profile").exists())
			.andDo(document("query-events"))
		;
		
	}
	
	@Test
	@TestDescription("기존의 이벤트를 하나 조회하기")
	public void getEvent() throws Exception {
		// Given
		Event event = this.generateEvent(100);
		
		// When & Then
		this.mockMvc.perform(get("/api/events/{id}", event.getId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").exists())
			.andExpect(jsonPath("id").exists())
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.profile").exists())
			.andDo(document("get-an-event"))
		;
	}


	@Test
	@TestDescription("없는 이벤트는 조회했을 때 404 응답받기")
	public void getEvent404() throws Exception{
		//When & Then
		this.mockMvc.perform(get("/api/events/11883"))
			.andExpect(status().isNotFound());
	}
	
	@Test
	@TestDescription("이벤트를 정상적으로 수정하기")
	public void updateEvent() throws Exception{
		// Given
		Event event = this.generateEvent(200);
		EventDto eventDto = this.modelMapper.map(event, EventDto.class);
		String eventName = "Update Event";
		eventDto.setName(eventName);
		
		this.mockMvc.perform(put("/api/events/{id}", event.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(eventDto))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value(eventName))
			.andExpect(jsonPath("_links.self").exists())
			.andDo(document("update-event"));
		;
	}
	
	@Test
	@TestDescription("입력값이 비어있는 경우에 이벤트 수정 실패")
	public void updateEvent400_Empty() throws Exception{
		// Given
		Event event = this.generateEvent(200);
		EventDto eventDto = new EventDto();
		
		this.mockMvc.perform(put("/api/events/{id}", event.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(eventDto))
				)
		.andDo(print())
		.andExpect(status().isBadRequest())
		;
	}
	
	@Test
	@TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패")
	public void updateEvent400_Wrong() throws Exception{
		// Given
		Event event = this.generateEvent(200);
		EventDto eventDto = this.modelMapper.map(event, EventDto.class);
		eventDto.setBasePrice(20000);
		eventDto.setMaxPrice(1000);
		
		this.mockMvc.perform(put("/api/events/{id}", event.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(eventDto))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
		;
	}
	
	
	@Test
	@TestDescription("존재하지 않는 이벤트 수정 실패")
	public void updateEvent400() throws Exception{
		// Given
		Event event = this.generateEvent(200);
		EventDto eventDto = this.modelMapper.map(event, EventDto.class);
		
		// When & Then
		this.mockMvc.perform(put("/api/events/11231")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(eventDto))
			)
			.andDo(print())
			.andExpect(status().isNotFound())
		;
	}
	
	private Event generateEvent(int index) {
		// TODO Auto-generated method stub
		Event event = Event.builder()
				.name("event " + index)
				.description("test event")
				.beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
				.closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
				.beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
				.endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 스타텁 팩토리")
				.free(false)
				.offline(true)
				.eventStatus(EventStatus.DRAFT)
				.build();
		return this.eventRepository.save(event);
	}
	
}
