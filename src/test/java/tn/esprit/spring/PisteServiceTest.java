package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllPistes() {
        // Arrange
        List<Piste> pistes = Arrays.asList(new Piste(), new Piste());
        when(pisteRepository.findAll()).thenReturn(pistes);

        // Act
        List<Piste> result = pisteServices.retrieveAllPistes();

        // Assert
        assertEquals(2, result.size());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        // Arrange
        Piste piste = new Piste();
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);

        // Act
        Piste result = pisteServices.addPiste(piste);

        // Assert
        assertEquals(piste, result);
        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    void testRemovePiste() {
        // Act
        pisteServices.removePiste(1L);

        // Assert
        verify(pisteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrievePiste() {
        // Arrange
        Piste piste = new Piste();
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));

        // Act
        Piste result = pisteServices.retrievePiste(1L);

        // Assert
        assertEquals(piste, result);
        verify(pisteRepository, times(1)).findById(1L);
    }
}
