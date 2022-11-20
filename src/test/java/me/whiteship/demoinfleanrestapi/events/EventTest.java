package me.whiteship.demoinfleanrestapi.events;

import junitparams.Parameters;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("jaemin")
                .description("park")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        Event event = new Event();
        String name = "JAEMIN";
        String description = "PARK";
        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }


    @Test
    @Parameters({
            "0,0,true",
            "100,0,false",
            "0,100,false",
            "100,200,false"
    }

    )
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private static Object[] parametersForTestFree(){
        return new Object[][] {
            new Object[] {0,0,true},
            new Object[] {100,0,false},
            new Object[] {0,100,false},
            new Object[] {100,200,false}
        };
    }

    @Test
    public void testOffline(){
        // Given
        Event event = Event.builder()
                .location("강남역 네이버 D2 스타트업 팩토리")
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isTrue();

        event = Event.builder()
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isFalse();
    }
}