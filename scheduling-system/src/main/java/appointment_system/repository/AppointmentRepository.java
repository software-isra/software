package appointment_system.repository;
import java.util.ArrayList;
import java.util.List;
import appointment_system.domain.Appointment;
import appointment_system.domain.AppointmentSlot;
public class AppointmentRepository {
private List<AppointmentSlot> slots = new ArrayList<>();
 private List<Appointment> appointments = new ArrayList<>();
public void addSlot(AppointmentSlot slot) {
        slots.add(slot);}
    public List<AppointmentSlot> getSlots() {
        return slots;
    }
public void saveAppointment(Appointment appointment) {
        appointments.add(appointment); }
    public List<Appointment> getAppointments() {
        return appointments;
    }
}
