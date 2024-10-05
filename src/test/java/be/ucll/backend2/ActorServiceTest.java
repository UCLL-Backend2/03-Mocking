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

@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {
    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorService actorService;

    @Test
    public void givenNoActorExistsWithId_whenActorIsDeleted_thenActorNotFoundExceptionIsThrown() {
        Mockito.doReturn(false).when(actorRepository).existsById(1L);
        // of:
        // Mockito.when(actorRepository.existsById(1L)).thenReturn(false);

        final var exception = Assertions.assertThrows(ActorNotFoundException.class, () -> actorService.deleteActor(1L));
        Assertions.assertEquals("Actor not found for id: 1", exception.getMessage());
    }

    @Test
    public void givenActorExistsWithId_whenActorIsDeleted_thenActorIsDeleted() throws ActorNotFoundException {
        Mockito.doReturn(true).when(actorRepository).existsById(1L);
        // of:
        // Mockito.when(actorRepository.existsById(1L)).thenReturn(true);

        actorService.deleteActor(1L);
        Mockito.verify(actorRepository).deleteById(1L);
    }

    @Test
    public void givenActorExistsWithId_whenGetActorById_thenReturnActor() throws ActorNotFoundException {
        final var actor = new Actor("Cillian Murphy");
        actor.setId(1L);

        // Voorbeeld zonder @Mock
        final var actorRepository = Mockito.mock(ActorRepository.class);

        Mockito.when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
        // of:
        // Mockito.doReturn(Optional.of(actor)).when(actorRepository).findById(1L);

        // Voorbeeld zonder @InjectMocks
        final var actorService = new ActorService(actorRepository);

        final var returnedActor = actorService.getActorById(1L);

        Assertions.assertEquals(actor, returnedActor);
    }

    @Test
    public void givenActorDoesNotExistWithId_whenGetActorById_thenActorNotFoundExceptionIsThrown() {
        // Voorbeeld zonder @Mock
        final var actorRepository = Mockito.mock(ActorRepository.class);

        Mockito.when(actorRepository.findById(1L)).thenReturn(Optional.empty());
        // of:
        // Mockito.doReturn(Optional.empty()).when(actorRepository).findById(1L);

        // Voorbeeld zonder @InjectMocks
        final var actorService = new ActorService(actorRepository);

        Assertions.assertThrows(ActorNotFoundException.class, () -> actorService.getActorById(1L));
    }
}
