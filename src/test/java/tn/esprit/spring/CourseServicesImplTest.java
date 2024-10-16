package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(5);
        course.setPrice(100.0f);
    }

    @Test
    void testRetrieveAllCourses() {
        // Arrange
        List<Course> courseList = Arrays.asList(course);
        when(courseRepository.findAll()).thenReturn(courseList);

        // Act
        List<Course> retrievedCourses = courseServices.retrieveAllCourses();

        // Assert
        assertNotNull(retrievedCourses);
        assertEquals(1, retrievedCourses.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testAddCourse() {
        // Arrange
        when(courseRepository.save(course)).thenReturn(course);

        // Act
        Course addedCourse = courseServices.addCourse(course);

        // Assert
        assertNotNull(addedCourse);
        assertEquals(course.getNumCourse(), addedCourse.getNumCourse());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testUpdateCourse() {
        // Arrange
        when(courseRepository.save(course)).thenReturn(course);

        // Act
        Course updatedCourse = courseServices.updateCourse(course);

        // Assert
        assertNotNull(updatedCourse);
        assertEquals(course.getNumCourse(), updatedCourse.getNumCourse());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testRetrieveCourse() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Act
        Course retrievedCourse = courseServices.retrieveCourse(1L);

        // Assert
        assertNotNull(retrievedCourse);
        assertEquals(1L, retrievedCourse.getNumCourse());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveCourse_NotFound() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Course retrievedCourse = courseServices.retrieveCourse(1L);

        // Assert
        assertNull(retrievedCourse);
        verify(courseRepository, times(1)).findById(1L);
    }
}
