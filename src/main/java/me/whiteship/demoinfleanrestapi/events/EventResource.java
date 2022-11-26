package me.whiteship.demoinfleanrestapi.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@AllArgsConstructor
@Getter
public class EventResource extends EntityModel<Event> {

    public EventResource(Event event, Link... links){
        super(event, Arrays.asList(links));
//        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class)
//                .slash(event.getId());
//        add(selfLinkBuilder.withSelfRel());
//        add(linkTo(EventController.class).withRel("query-events"));
//        add(selfLinkBuilder.withRel("update-event"));

    }

}
