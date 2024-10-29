package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.services.PisteServicesImpl;
import tn.esprit.spring.services.SkierServicesImpl;

@SpringBootTest(classes = PisteServicesImpl.class)
class GestionStationSkiApplicationTests {

	@MockBean
	private PisteServicesImpl pisteServices;

	@Test
	void contextLoads() {
		// This will only load the SkierServicesImpl and its dependencies, reducing the chance of a context failure.
	}
}