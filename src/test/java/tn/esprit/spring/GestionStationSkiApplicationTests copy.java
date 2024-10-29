@SpringBootTest
public class GestionStationSkiApplicationTests {

    @MockBean
    private PisteServicesImpl pisteServices;

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads without issues.
    }
}
