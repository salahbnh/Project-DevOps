package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.SkierServicesImpl;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.profiles.active=docker"})
public class SkierServiceIntegrationTest {

    @Autowired
    private SkierServicesImpl skierServices;

    @Test
    public void testAddSkier() {
        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setCity("CityX");
        skier.setDateOfBirth(LocalDate.of(2000, 1, 1));

        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());
        skier.setSubscription(subscription);

        Skier savedSkier = skierServices.addSkier(skier);

        assertNotNull(savedSkier);
        assertNotNull(savedSkier.getNumSkier());
        assertEquals("John", savedSkier.getFirstName());
        assertEquals("Doe", savedSkier.getLastName());
    }
}
