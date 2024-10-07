package be.ucll.backend2;

import be.ucll.backend2.exception.ActorNotFoundException;
import be.ucll.backend2.model.Actor;
import be.ucll.backend2.repository.ActorRepository;
import be.ucll.backend2.service.ActorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

// MockitoExtension: enables @Mock en @InjectMocks
@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {
    // @Mock: zet mock op voor elke test
    @Mock
    private ActorRepository actorRepository;

    // @InjectMocks: maak een actorService voor elke test, voorzie een mock alle argumenten
    @InjectMocks
    private ActorService actorService;

    @Test
    public void givenNoActorExistsWithId_whenActorIsDeleted_thenActorNotFoundExceptionIsThrown() {
        // Mock existsById methode
        Mockito.when(actorRepository.existsById(1L)).thenReturn(false);
        // of:
        // Mockito.doReturn(false).when(actorRepository).existsById(1L);

        // Call de methode onder test (moet throwen)
        final var exception = Assertions.assertThrows(ActorNotFoundException.class, () -> actorService.deleteActor(1L));

        // Check of exception klopt
        Assertions.assertEquals("Actor not found for id: 1", exception.getMessage());
    }

    @Test
    public void givenActorExistsWithId_whenActorIsDeleted_thenActorIsDeleted() throws ActorNotFoundException {
        // Mock existsById methode
        Mockito.when(actorRepository.existsById(1L)).thenReturn(true);
        // of:
        // Mockito.doReturn(true).when(actorRepository).existsById(1L);

        // Call de methode onder test
        actorService.deleteActor(1L);

        // Kijk na of de deleteById methode gecalld is
        Mockito.verify(actorRepository).deleteById(1L);
    }

    // - Implementeer service test(s) voor getActorById
    //   - actor in repository
    @Test
    public void givenActorsExistsWithId_whenGetActorByIdIsCalled_thenActorIsReturned() throws ActorNotFoundException {
        // Given
        final var actor = new Actor("Cillian Murphy");
        actor.setId(1L);

        Mockito.when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));

        // When
        final var returnedActor = actorService.getActorById(1L);

        // Then
        Assertions.assertEquals(actor, returnedActor);
    }

    //   - actor niet in repository
    @Test
    public void givenActorDoesNotExistWithId_whenGetActorByIdIsCalled_thenActorNotFoundExceptionIsThrown() {
        // Given
        Mockito.when(actorRepository.findById(1L)).thenReturn(Optional.empty());

        // When (and then)
        final var exception = Assertions.assertThrows(ActorNotFoundException.class, () -> actorService.getActorById(1L));

        // Then
        Assertions.assertEquals("Actor not found for id: 1", exception.getMessage());
    }

    // - Implementeer service test(s) voor createActor
    @Test
    public void givenActorIsNotYetInDatabase_whenCreateIsActorIsCalled_thenActorIsSaved() {
        // Given
        final var actorToReturn = new Actor("Cillian Murphy");
        actorToReturn.setId(1L);

        final var actorToSave = new Actor("Cillian Murphy");

        Mockito.when(actorRepository.save(actorToSave)).thenReturn(actorToReturn);

        // When
        final var returnedActor = actorService.createActor(actorToSave);

        // Then
        Assertions.assertEquals(actorToReturn, returnedActor);
        Mockito.verify(actorRepository).save(actorToSave);
    }

    // - Implementeer service test(s) voor updateActor
    @Test
    public void givenActorIsInDatabase_whenUpdateActorIsCalled_thenActorIsUpdated() throws ActorNotFoundException {
        final var actorInDb = new Actor("Cillian Murphy");
        actorInDb.setId(1L);

        Mockito.when(actorRepository.findById(1L)).thenReturn(Optional.of(actorInDb));

        final var updatedActor = new Actor("Tom Hanks");
        updatedActor.setId(1L);

        Mockito.when(actorRepository.save(updatedActor)).thenReturn(updatedActor);

        final var returnedActor = actorService.updateActor(1L, new Actor("Tom Hanks"));

        Assertions.assertEquals(updatedActor, returnedActor);
        Mockito.verify(actorRepository).save(updatedActor);
    }

    // Oefening:
    // - Implementeer service test(s) voor getAllActors
    // Let op: zorg ervoor dat elk geval getest is (happy cases en unhappy cases!)
}
