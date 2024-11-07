package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SkierServicesImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.profiles.active=docker"})
@Transactional
public class SkierServiceIntegrationTest {

    @Autowired
    private SkierServicesImpl skierServices;

    @Autowired
    private ISkierRepository skierRepository;

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private ISubscriptionRepository subscriptionRepository;

    @Autowired
    private IRegistrationRepository registrationRepository;

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

        assertNotNull(savedSkier.getNumSkier());
        assertEquals("John", savedSkier.getFirstName());
        assertEquals("Doe", savedSkier.getLastName());
        assertEquals(TypeSubscription.ANNUAL, savedSkier.getSubscription().getTypeSub());
        assertNotNull(savedSkier.getSubscription().getEndDate());
    }

    @Test
    public void testAddSkierAndAssignToCourse() {
        Skier skier = new Skier();
        skier.setFirstName("Alice");
        skier.setLastName("Green");
        skier.setCity("CityZ");
        skier.setDateOfBirth(LocalDate.of(1998, 3, 20));
        skierRepository.save(skier);

        Course course = new Course();
        courseRepository.save(course);

        Skier savedSkier = skierServices.addSkierAndAssignToCourse(skier, course.getNumCourse());
        assertNotNull(savedSkier);
        assertEquals(course.getNumCourse(), savedSkier.getRegistrations().iterator().next().getCourse().getNumCourse());
    }

    @Test
    public void testRemoveSkier() {
        Skier skier = new Skier();
        skier.setFirstName("Bob");
        skier.setLastName("Brown");
        skierRepository.save(skier);

        Long skierId = skier.getNumSkier();
        skierServices.removeSkier(skierId);

        assertFalse(skierRepository.existsById(skierId));
    }

    @Test
    public void testRetrieveSkier() {
        Skier skier = new Skier();
        skier.setFirstName("Carol");
        skier.setLastName("White");
        skierRepository.save(skier);

        Skier retrievedSkier = skierServices.retrieveSkier(skier.getNumSkier());
        assertNotNull(retrievedSkier);
        assertEquals("Carol", retrievedSkier.getFirstName());
    }


    @Test
    public void testRetrieveAllSkiers() {
        Skier skier1 = new Skier();
        skier1.setFirstName("Eve");
        skier1.setLastName("Black");
        skierRepository.save(skier1);

        Skier skier2 = new Skier();
        skier2.setFirstName("Frank");
        skier2.setLastName("Green");
        skierRepository.save(skier2);

        List<Skier> skiers = skierServices.retrieveAllSkiers();
        assertTrue(skiers.size() >= 2);
    }

    @Test
    public void testRetrieveSkiersBySubscriptionType() {
        Skier skier1 = new Skier();
        Subscription subscription1 = new Subscription();
        subscription1.setTypeSub(TypeSubscription.ANNUAL);
        skier1.setSubscription(subscription1);
        skierRepository.save(skier1);

        Skier skier2 = new Skier();
        Subscription subscription2 = new Subscription();
        subscription2.setTypeSub(TypeSubscription.MONTHLY);
        skier2.setSubscription(subscription2);
        skierRepository.save(skier2);

        List<Skier> annualSkiers = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);
        assertTrue(annualSkiers.stream().anyMatch(s -> s.getSubscription().getTypeSub() == TypeSubscription.ANNUAL));
    }
}
