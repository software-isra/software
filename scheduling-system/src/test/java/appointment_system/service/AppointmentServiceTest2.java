package appointment_system.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import appointment_system.domain.AppointmentSlot;
import appointment_system.repository.AppointmentRepository;
class AppointmentServiceTest {

private AppointmentService service;
private AppointmentRepository repo;

 @BeforeEach
void setup() {
 repo = new AppointmentRepository();
service = new AppointmentService(repo); }

    @Test
void testGetAvailableSlots() {
 // GIVEN
AppointmentSlot slot1 =new AppointmentSlot(LocalDateTime.now(), 60, 1);

AppointmentSlot slot2 =new AppointmentSlot(LocalDateTime.now(), 60, 1);
       
slot2.addParticipant(); // full
repo.addSlot(slot1);
 repo.addSlot(slot2);

// WHEN
 List<AppointmentSlot> result =service.getAvailableSlots();

 // THEN
int expected = 1;
int actual = result.size();
 assertEquals(expected, actual);
    }

    @Test
 void testBookAppointment() {
// GIVEN
 AppointmentSlot slot =  new AppointmentSlot(LocalDateTime.now(), 60, 1);
  repo.addSlot(slot);

 // WHEN
 boolean actual = service.bookAppointment("user1", slot);

// THEN
 boolean expected = true;
  assertEquals(expected, actual);
    }

    // full slot
    @Test
    void testBookAppointmentWhenFull() {

// GIVEN
AppointmentSlot slot =new AppointmentSlot(LocalDateTime.now(), 60, 1);
 slot.addParticipant(); // full
  repo.addSlot(slot);

   // WHEN
boolean actual = service.bookAppointment("user1", slot);

 // THEN
 boolean expected = false;
 assertEquals(expected, actual);
    }

   // duration rule
    @Test
  void testInvalidDuration() {

        // GIVEN
 AppointmentSlot slot =  new AppointmentSlot(LocalDateTime.now(), 200, 2);
        repo.addSlot(slot);

        // THEN
        assertThrows(IllegalArgumentException.class, () -> {
            service.bookAppointment("user1", slot);
        });
    }}