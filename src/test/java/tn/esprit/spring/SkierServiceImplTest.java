package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SkierServiceImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddSkierWithAnnualSubscription() {
        // Arrange
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        skier.setSubscription(subscription);

        // Simuler le comportement du repository
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // Act
        Skier result = skierServices.addSkier(skier);

        // Assert
        assertEquals(LocalDate.of(2025, 1, 1), result.getSubscription().getEndDate()); // L'abonnement annuel doit durer 1 an
        verify(skierRepository, times(1)).save(skier); // Vérifie que save() a été appelé une fois
    }

    @Test
    void testAddSkierWithMonthlySubscription() {
        // Arrange
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        subscription.setTypeSub(TypeSubscription.MONTHLY);
        skier.setSubscription(subscription);

        // Simuler le comportement du repository
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // Act
        Skier result = skierServices.addSkier(skier);

        // Assert
        assertEquals(LocalDate.of(2024, 2, 1), result.getSubscription().getEndDate()); // L'abonnement mensuel doit durer 1 mois
        verify(skierRepository, times(1)).save(skier); // Vérifie que save() a été appelé une fois
    }

    @Test
    void testAddSkierWithSemestrialSubscription() {
        // Arrange
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        subscription.setTypeSub(TypeSubscription.SEMESTRIEL);
        skier.setSubscription(subscription);

        // Simuler le comportement du repository
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // Act
        Skier result = skierServices.addSkier(skier);

        // Assert
        assertEquals(LocalDate.of(2024, 7, 1), result.getSubscription().getEndDate()); // L'abonnement semestriel doit durer 6 mois
        verify(skierRepository, times(1)).save(skier); // Vérifie que save() a été appelé une fois
    }

    @Test
    void testRetrieveAllSkiers() {
        // Arrange
        List<Skier> skiers = Arrays.asList(new Skier(), new Skier());
        when(skierRepository.findAll()).thenReturn(skiers);

        // Act
        List<Skier> result = skierServices.retrieveAllSkiers();

        // Assert
        assertEquals(2, result.size());
        verify(skierRepository, times(1)).findAll();
    }

    // Test for assignSkierToSubscription()
    @Test
    void testAssignSkierToSubscription() {
        // Arrange
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(2L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // Act
        Skier result = skierServices.assignSkierToSubscription(1L, 2L);

        // Assert
        assertEquals(subscription, result.getSubscription());
        verify(skierRepository, times(1)).save(skier);
    }

    // Test for addSkierAndAssignToCourse()
    @Test
    void testAddSkierAndAssignToCourse() {
        // Arrange
        Skier skier = new Skier();
        Registration registration = new Registration();
        Set<Registration> registrations = new HashSet<>();
        registrations.add(registration);
        skier.setRegistrations(registrations);

        Course course = new Course();

        when(skierRepository.save(any(Skier.class))).thenReturn(skier);
        when(courseRepository.getById(1L)).thenReturn(course);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Act
        Skier result = skierServices.addSkierAndAssignToCourse(skier, 1L);

        // Assert
        assertEquals(course, registration.getCourse());
        verify(skierRepository, times(1)).save(skier);
        verify(registrationRepository, times(1)).save(registration);
    }


    // Test for removeSkier()
    @Test
    void testRemoveSkier() {
        // Act
        skierServices.removeSkier(1L);

        // Assert
        verify(skierRepository, times(1)).deleteById(1L);
    }

    // Test for retrieveSkier()
    @Test
    void testRetrieveSkier() {
        // Arrange
        Skier skier = new Skier();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        // Act
        Skier result = skierServices.retrieveSkier(1L);

        // Assert
        assertEquals(skier, result);
        verify(skierRepository, times(1)).findById(1L);
    }

    // Test for assignSkierToPiste()
    @Test
    void testAssignSkierToPiste() {
        // Arrange
        Skier skier = new Skier();
        Piste piste = new Piste();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(2L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // Act
        Skier result = skierServices.assignSkierToPiste(1L, 2L);

        // Assert
        assertTrue(result.getPistes().contains(piste));
        verify(skierRepository, times(1)).save(skier);
    }

    // Test for retrieveSkiersBySubscriptionType()
    @Test
    void testRetrieveSkiersBySubscriptionType() {
        // Arrange
        List<Skier> skiers = Arrays.asList(new Skier(), new Skier());
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL)).thenReturn(skiers);

        // Act
        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);

        // Assert
        assertEquals(2, result.size());
        verify(skierRepository, times(1)).findBySubscription_TypeSub(TypeSubscription.ANNUAL);
    }
}
