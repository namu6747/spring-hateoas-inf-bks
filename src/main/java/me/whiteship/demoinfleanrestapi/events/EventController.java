package me.whiteship.demoinfleanrestapi.events;

import lombok.RequiredArgsConstructor;
import me.whiteship.demoinfleanrestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
            System.out.println("1차 에러");
            return badRequest(bindingResult);
        }

        eventValidator.validate(eventDto,bindingResult);
        if(bindingResult.hasErrors()){
            System.out.println("2차 에러");
            return badRequest(bindingResult);
        }
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);

        EventResource eventResource = new EventResource(event);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(event.getId());
        URI createdUri = selfLinkBuilder.toUri();
        eventResource.add(
                //selfLinkBuilder.withSelfRel(),
                linkTo(EventController.class).withRel("query-events"),
                selfLinkBuilder.withRel("update-event"),
                Link.of("/docs/index.html#resource-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){
        Page<Event> page = this.eventRepository.findAll(pageable);
        var pagedModel = assembler.toModel(page, e -> new EventResource(e));
        pagedModel.add(Link.of("/docs/index.html#resource-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id){
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resource-events-list").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id, 
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors){
        Optional<Event> optionalEvent = this.eventRepository.findById(id);

        if(optionalEvent.isEmpty()) return ResponseEntity.notFound().build();

        if(errors.hasErrors()) return badRequest(errors);

        this.eventValidator.validate(eventDto,errors);

        if(errors.hasErrors()) return badRequest(errors);

        Event existingEvent = optionalEvent.get();

        this.modelMapper.map(eventDto, existingEvent);

        Event savedEvent = this.eventRepository.save(existingEvent);

        EntityModel<Event> eventResource = new EventResource(savedEvent);

        eventResource.add(Link.of("/docs/index.html#resource-events-list").withRel("profile"));

        return ResponseEntity.ok(eventResource);
    }


    private ResponseEntity<EntityModel<Errors>> badRequest(Errors bindingResult) {
        return ResponseEntity.badRequest().body(ErrorsResource.modelOf(bindingResult));
    }
}
