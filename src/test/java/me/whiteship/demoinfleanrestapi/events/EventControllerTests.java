package me.whiteship.demoinfleanrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.whiteship.demoinfleanrestapi.common.RestDocsConfiguration;
import me.whiteship.demoinfleanrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTests {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 실행")
    public void createEvent() throws Throwable{
        Event event = Event.builder()
                        .id(100)
                        .name("name")
                        .description("rest api")
                        .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                        .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                        .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                        .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                        .basePrice(100)
                        .maxPrice(200)
                        .free(true)
                        .limitOfEnrollment(100)
                        .location("강남역 D2")
                        .eventStatus(EventStatus.PUBLISHED)
                        .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
//                        .contentType(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists("Location"))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string("Content-Type","application/hal+json"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.PUBLISHED)))
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("Description of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment of new Event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new Event"),
                                fieldWithPath("endEventDateTime").description("EndEventDateTime of new Event"),
                                fieldWithPath("location").description("Location of new Event"),
                                fieldWithPath("basePrice").description("base price of new Event"),
                                fieldWithPath("maxPrice").description("MaxPrice of new Event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment of new Event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("response location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("response content type header")
                        ),
//                        relaxedResponseFields(
                        responseFields(
                                fieldWithPath("id").description("Identifier of new Event"),
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("Description of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment of new Event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new Event"),
                                fieldWithPath("endEventDateTime").description("EndEventDateTime of new Event"),
                                fieldWithPath("location").description("Location of new Event"),
                                fieldWithPath("basePrice").description("base price of new Event"),
                                fieldWithPath("maxPrice").description("MaxPrice of new Event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment of new Event"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events"),
                                fieldWithPath("_links.update-event.href").description("link to update event"),
                                fieldWithPath("_links.profile.href").description("link to profile")

                        )
                        ))
        ;

    }

    @Test
    public void createEvent_badRequest() throws Throwable{
        Event event = Event.builder()
                        .name("name")
                        .description("rest api")
                        .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                        .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                        .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                        .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                        .basePrice(100)
                        .maxPrice(200)
                        .limitOfEnrollment(100)
                        .location("강남역 D2")
                        .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
//                .andExpect(status().isBadRequest())
        ;

    }

//    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                ;
    }

    @Test
    public void createEvent_Bad_Request_Wrong_Input() throws Exception{
        EventDto eventDto = EventDto.builder()
                .name("name")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,26,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,28,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .build();
        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
//                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
//                .andExpect(jsonPath("$[0].rejectedValue").exists())
                ;
    }

}
