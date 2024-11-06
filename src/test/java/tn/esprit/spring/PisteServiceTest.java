
package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.IPisteServices;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PisteServiceTest {

	@Mock
	private IPisteRepository pisteRepository;

	@InjectMocks
	private IPisteServices pisteService; // Assuming this is the concrete implementation of IPisteServices

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRetrieveAllPistes() {
		// Arrange
		Piste piste1 = new Piste();
		piste1.setNumPiste(1L);
		piste1.setNamePiste("Piste1");
		piste1.setColor(Color.BLUE);
		piste1.setLength(300);
		piste1.setSlope(15);

		Piste piste2 = new Piste();
		piste2.setNumPiste(2L);
		piste2.setNamePiste("Piste2");
		piste2.setColor(Color.RED);
		piste2.setLength(400);
		piste2.setSlope(20);

		List<Piste> expectedPistes = Arrays.asList(piste1, piste2);

		when(pisteRepository.findAll()).thenReturn(expectedPistes);

		// Act
		List<Piste> actualPistes = pisteService.retrieveAllPistes();

		// Assert
		assertEquals(expectedPistes.size(), actualPistes.size());
		verify(pisteRepository, times(1)).findAll();
	}


	@Test
	void testAddPiste() {
		// Arrange
		Piste newPiste = new Piste();
		newPiste.setNumPiste(null);
		newPiste.setNamePiste("NewPiste");
		newPiste.setColor(Color.GREEN);
		newPiste.setLength(350);
		newPiste.setSlope(10);

		Piste savedPiste = new Piste();
		savedPiste.setNumPiste(1L);
		savedPiste.setNamePiste("NewPiste");
		savedPiste.setColor(Color.GREEN);
		savedPiste.setLength(350);
		savedPiste.setSlope(10);

		when(pisteRepository.save(any(Piste.class))).thenReturn(savedPiste);

		// Act
		Piste result = pisteService.addPiste(newPiste);

		// Assert
		assertNotNull(result);
		assertEquals(savedPiste.getNumPiste(), result.getNumPiste());
		verify(pisteRepository, times(1)).save(newPiste);
	}

	@Test
	void testRetrievePiste() {
		// Arrange
		Long pisteId = 1L;
		Piste expectedPiste = new Piste();
		expectedPiste.setNumPiste(pisteId);
		expectedPiste.setNamePiste("Piste1");
		expectedPiste.setColor(Color.BLUE);
		expectedPiste.setLength(300);
		expectedPiste.setSlope(15);

		when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(expectedPiste));

		// Act
		Piste actualPiste = pisteService.retrievePiste(pisteId);

		// Assert
		assertNotNull(actualPiste);
		assertEquals(expectedPiste.getNumPiste(), actualPiste.getNumPiste());
		verify(pisteRepository, times(1)).findById(pisteId);
	}

}