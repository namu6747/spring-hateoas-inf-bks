package me.whiteship.demoinfleanrestapi.events;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value="/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult);
        }

        eventValidator.validate(eventDto,bindingResult);
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult);
        }
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);

        EventResource eventResource = new EventResource(event);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(event.getId());
        URI createdUri = selfLinkBuilder.toUri();
        eventResource.add(
                selfLinkBuilder.withSelfRel(),
                linkTo(EventController.class).withRel("query-events"),
                selfLinkBuilder.withRel("update-event"),
                Link.of("/docs/index.html#resource-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

//    @GetMapping("test")
//    private ResponseEntity testMapping(){
//        return ResponseEntity.ok("hello");
//    }

}
