package appointment_system.service;
import java.util.List;
import appointment_system.domain.Appointment;
import appointment_system.domain.AppointmentSlot;
import appointment_system.repository.AppointmentRepository;
public class AppointmentService {
	private AppointmentRepository repository;

    private static final int MAX_DURATION = 120;
    public AppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }
 public List<AppointmentSlot> getAvailableSlots() {
 return repository.getSlots()
  .stream()
  .filter(slot -> !slot.isFull())
.toList();
    }
    public boolean bookAppointment(String username, AppointmentSlot slot) {
        if (slot.getDurationMinutes() > MAX_DURATION) {
            throw new IllegalArgumentException("Duration exceeds allowed limit");
        }
 if (slot.isFull()) {
 return false; }
 slot.addParticipant();
   Appointment appointment = new Appointment(slot, username);
  repository.saveAppointment(appointment);
     return true;
    }
}

