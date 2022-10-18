package sept.superfive.appointmentmicroservice.unit_test.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import sept.superfive.appointmentmicroservice.dto.AvailabilityDTO;
import sept.superfive.appointmentmicroservice.exception.InvalidTimeException;
import sept.superfive.appointmentmicroservice.exception.ResourceAlreadyExistsException;
import sept.superfive.appointmentmicroservice.model.Availability;
import sept.superfive.appointmentmicroservice.repository.AvailabilityRepository;
import sept.superfive.appointmentmicroservice.service.AvailabilityService;

import java.util.*;

class
AvailabilityServiceUnitTest {

    private AvailabilityRepository mockAvailabilityRepository;
    private AvailabilityService availabilityService;

    private Availability availability1;
    private Availability availability2;

    private final List<Availability> availabilities = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockAvailabilityRepository = Mockito.mock(AvailabilityRepository.class);
        availabilityService = new AvailabilityService(mockAvailabilityRepository);

        availability1 = new Availability(1, DayOfWeek.MONDAY,
                LocalTime.of(9, 0, 0), LocalTime.of(17, 0, 0),1);
        availability2 = new Availability(2, DayOfWeek.FRIDAY,
                LocalTime.of(12, 0, 0), LocalTime.of(22, 0, 0),1);

        availabilities.add(availability1);
        availabilities.add(availability2);
    }

    @Test
    void getAllAvailabilities() {
        when(mockAvailabilityRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(availability1, availability2))));

        Page<Availability> availabilityPage = availabilityService.getAllAvailabilities(null);  // Get doctor page
        List<Availability> retrievedAvailabilities = availabilityPage.getContent();

        assertEquals(availabilities.size(), retrievedAvailabilities.size());

        for (int i = 0; i < availabilities.size(); i++) {
            assertEquals(availabilities.get(i), retrievedAvailabilities.get(i));
        }
    }

    @Test
    void getAvailabilityByID() {
        when(mockAvailabilityRepository.findByAvailabilityID(availability1.getAvailabilityID())).thenReturn(Optional.of(availability1));
        Availability testAvailability = availabilityService.getAvailabilityByID(availability1.getAvailabilityID());
        assertEquals(testAvailability, availability1);
    }

    @Test
    void getDoctorAvailabilities() {
        when(mockAvailabilityRepository.findByDoctorID(availability1.getDoctorID(),null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(availability1))));

        Page<Availability> availabilitiesDoctorPage = availabilityService.getDoctorAvailabilities(availability1.getDoctorID(),null);  // Get doctor page
        List<Availability> doctorAvailability = availabilitiesDoctorPage.getContent();

        assertEquals(1, doctorAvailability.size());

        assertEquals(availability1, doctorAvailability.get(0));
    }

    @Test
    void addAvailability() {
        AvailabilityDTO invalidTimeAvailabilityDTO = new AvailabilityDTO(3,
                2, LocalTime.of(11, 0, 0), LocalTime.of(10, 0, 0),
                1);

        assertThrows(InvalidTimeException.class, () -> availabilityService.addAvailability(invalidTimeAvailabilityDTO));

        AvailabilityDTO availability3DTO = new AvailabilityDTO(3,
                2, LocalTime.of(10, 0, 0), LocalTime.of(11, 0, 0),
                1);

        when(mockAvailabilityRepository.findByDoctorIDAndDayOfWeek(availability3DTO.getDoctorID(),
                        DayOfWeek.of(availability3DTO.getDayOfWeek()))).thenReturn(Optional.ofNullable(availability1));

        assertThrows(ResourceAlreadyExistsException.class, () -> availabilityService.addAvailability(availability3DTO));

        AvailabilityDTO availability4DTO = new AvailabilityDTO(4, 2,
                LocalTime.of(10, 0, 0), LocalTime.of(11, 0, 0), 1);

        when(mockAvailabilityRepository.findByDoctorIDAndDayOfWeek(availability4DTO.getDoctorID(),
                        DayOfWeek.of(availability4DTO.getDayOfWeek()))).thenReturn(Optional.empty());

        Availability availability4 = availabilityService.addAvailability(availability4DTO);

        assertEquals(availability4DTO.getAvailabilityID(), availability4.getAvailabilityID());
        assertEquals(availability4DTO.getStartTime(), availability4.getStartTime());
        assertEquals(availability4DTO.getEndTime(), availability4.getEndTime());
        assertEquals(availability4DTO.getDayOfWeek(), availability4.getDayOfWeek().getValue());
        assertEquals(availability4DTO.getDoctorID(), availability4.getDoctorID());
    }

    @Test
    void rescheduleAvailability() {
        when(mockAvailabilityRepository.findByAvailabilityID(availability1.getAvailabilityID())).thenReturn(Optional.of(availability1));

        AvailabilityDTO invalidTimeAvailabilityDTO = new AvailabilityDTO(3,
                2, LocalTime.of(11, 0, 0), LocalTime.of(10, 0, 0),
                1);

        assertThrows(InvalidTimeException.class,
                () -> availabilityService.rescheduleAvailability(invalidTimeAvailabilityDTO, 1));

        AvailabilityDTO availabilityDTO = new AvailabilityDTO(1, 4,
                LocalTime.of(8, 0, 0), LocalTime.of(20, 0, 0), 2);

        Availability oldAvailability1 = new Availability(availability1.getAvailabilityID(), availability1.getDayOfWeek(),
                availability1.getStartTime(), availability1.getEndTime(), availability1.getDoctorID());

        when(mockAvailabilityRepository.save(availability1)).thenAnswer(i -> {
            availability1.setStartTime(availabilityDTO.getStartTime());
            availability1.setEndTime(availabilityDTO.getEndTime());
            availability1.setDayOfWeek(DayOfWeek.of(availabilityDTO.getDayOfWeek()));
            availability1.setDoctorID(availabilityDTO.getDoctorID());
            return availability1;
        });

        availabilityService.rescheduleAvailability(availabilityDTO, availability1.getAvailabilityID());

        assertEquals(availabilityDTO.getAvailabilityID(), availability1.getAvailabilityID());
        assertEquals(DayOfWeek.of(availabilityDTO.getDayOfWeek()), availability1.getDayOfWeek());
        assertEquals(availabilityDTO.getStartTime(), availability1.getStartTime());
        assertEquals(availabilityDTO.getEndTime(), availability1.getEndTime());
        assertEquals(availabilityDTO.getDoctorID(), availability1.getDoctorID());

        assertNotEquals(oldAvailability1.getDayOfWeek(), availability1.getDayOfWeek());
        assertNotEquals(oldAvailability1.getStartTime(), availability1.getStartTime());
        assertNotEquals(oldAvailability1.getEndTime(), availability1.getEndTime());
        assertNotEquals(oldAvailability1.getDoctorID(), availability1.getDoctorID());
    }

    @Test
    void deleteAvailability() {
        when(mockAvailabilityRepository.findByAvailabilityID(availability1.getAvailabilityID())).thenReturn(Optional.of(availability1));

        doNothing().when(mockAvailabilityRepository).deleteById(availability1.getAvailabilityID());

        Availability deletedAvailability = availabilityService.deleteAvailability(availability1.getAvailabilityID());

        when(mockAvailabilityRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(availability2))));

        Page<Availability> availabilityPage = availabilityService.getAllAvailabilities(null);
        List<Availability> retrievedAvailabilities = availabilityPage.getContent();

        // Test availability list length
        assertEquals(availabilities.size() - 1, retrievedAvailabilities.size());

        // Test that availability1 has been deleted
        assertEquals(availability2, retrievedAvailabilities.get(0));

        // Test that deleted availability matches availability1
        assertEquals(availability1, deletedAvailability);
    }
}