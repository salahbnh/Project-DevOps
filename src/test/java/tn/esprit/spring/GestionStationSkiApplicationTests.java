package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tn.esprit.spring.services.CourseServicesImpl;
import tn.esprit.spring.services.ICourseServices;

@SpringBootTest(classes = CourseServicesImpl.class)
class GestionStationSkiApplicationTests {

	@MockBean
	private CourseServicesImpl courseServices;

	@Test
	void contextLoads() {


	}

}
