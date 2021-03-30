package com.example.demo.common;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors>{

	@Override
	public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// TODO Auto-generated method stub
		gen.writeFieldName("content");
		gen.writeStartArray();
	
		errors.getFieldErrors().stream().forEach(e ->{
			try {//fileld message
				gen.writeStartObject();
				gen.writeStringField("field",e.getField());
				gen.writeStringField("objectName",e.getObjectName());
				gen.writeStringField("code",e.getCode());
				gen.writeStringField("defaultMessage",e.getDefaultMessage());
				Object rejectedValue = e.getRejectedValue();
				if(rejectedValue != null) {
					gen.writeStringField("rejectedValue", rejectedValue.toString());
				}
				gen.writeEndObject();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		errors.getGlobalErrors().stream().forEach(e -> {
			try {//global message
				gen.writeStartObject();
				gen.writeStringField("objectName",e.getObjectName());
				gen.writeStringField("code",e.getCode());
				gen.writeStringField("defaultMessage",e.getDefaultMessage());
				gen.writeEndObject();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		gen.writeEndArray();
		
	}


}
