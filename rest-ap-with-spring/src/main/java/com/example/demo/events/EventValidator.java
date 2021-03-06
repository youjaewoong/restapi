package com.example.demo.events;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {
	
	public void validate(EventDto eventDto, Errors errors) {
		if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
			//errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong.");
			//errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong."); //필드 에러
			errors.reject("wrongProices", "Values fo prices are wrong"); //글러벌 에러
		}
		
		LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
		if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
			endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
			endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
				errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
		}
		
		// TDDO BeginEventDateTime
		// TODO CloseEnrollmentDateTime
	}
}
