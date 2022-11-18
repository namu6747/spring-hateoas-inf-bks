package me.whiteship.demoinfleanrestapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        errors.getFieldErrors()
                .forEach(e -> {
                    try {
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeStringField("field",e.getField());
                        jsonGenerator.writeStringField("objectName",e.getObjectName());
                        jsonGenerator.writeStringField("code",e.getCode());
                        jsonGenerator.writeStringField("defaultMessage",e.getDefaultMessage());
                        Object rejectedValue = e.getRejectedValue();
                        if(rejectedValue != null){
                            jsonGenerator.writeStringField("rejectedValue",rejectedValue.toString());
                        }
                        jsonGenerator.writeEndObject();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    }
                });

        errors.getGlobalErrors().forEach(e -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName",e.getObjectName());
                jsonGenerator.writeStringField("code",e.getCode());
                jsonGenerator.writeStringField("defaultMessage",e.getDefaultMessage());
                jsonGenerator.writeEndObject();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });
        jsonGenerator.writeEndArray();

    }
}
