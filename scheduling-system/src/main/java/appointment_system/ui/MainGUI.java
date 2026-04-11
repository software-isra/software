package appointment_system.ui;

import appointment_system.domain.Appointment;
import appointment_system.domain.AppointmentSlot;
import appointment_system.domain.AppointmentType;
import appointment_system.repository.AppointmentRepository;
import appointment_system.repository.UserRepository;
import appointment_system.service.AdminReservationService;
import appointment_system.service.AppointmentService;
import appointment_system.service.AuthenticationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Final GUI for the Appointment Scheduling System.
 *
 * @author Team 3
 * @version 4.0
 */
public class MainGUI extends JFrame {

    private final AppointmentService appointmentService;
    private final AuthenticationService authService;
    private final AdminReservationService adminReservationService;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private JPanel userGridPanel;
    private DefaultTableModel adminTableModel;
    private JTable adminTable;

    private JLabel totalSlotsLabel;
    private JLabel availableSlotsLabel;
    private JLabel fullSlotsLabel;

    private JComboBox<String> filterComboBox;

    private final Color COLOR_PRIMARY = new Color(33, 47, 61);
    private final Color COLOR_ACCENT = new Color(52, 152, 219);
    private final Color COLOR_BG = new Color(242, 244, 244);

    private final Color COLOR_LOW = new Color(46, 204, 113);
    private final Color COLOR_MEDIUM = new Color(241, 196, 15);
    private final Color COLOR_HIGH = new Color(231, 76, 60);
    private final Color COLOR_FULL = new Color(149, 165, 166);

    public MainGUI(AuthenticationService authService,
                   AppointmentService appointmentService,
                   AdminReservationService adminReservationService) {
        this.authService = authService;
        this.appointmentService = appointmentService;
        this.adminReservationService = adminReservationService;

        setTitle("Appointment Management System");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(createStartScreen(), "START");
        cardPanel.add(createAdminLoginView(), "ADMIN_LOGIN");
        cardPanel.add(createAdminDashboard(), "ADMIN_DASHBOARD");
        cardPanel.add(createUserBookingView(), "USER_VIEW");

        add(cardPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createStartScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(false);

        JLabel title = new JLabel("Appointment Management Portal");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(COLOR_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Smart scheduling with booking indicators and type selection");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(Color.DARK_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnUser = createStyledButton("Customer View", COLOR_ACCENT);
        JButton btnAdmin = createStyledButton("Administration Access", COLOR_PRIMARY);

        btnUser.addActionListener(e -> {
            refreshUserSlots();
            cardLayout.show(cardPanel, "USER_VIEW");
        });

        btnAdmin.addActionListener(e -> cardLayout.show(cardPanel, "ADMIN_LOGIN"));

        box.add(title);
        box.add(Box.createRigidArea(new Dimension(0, 10)));
        box.add(subtitle);
        box.add(Box.createRigidArea(new Dimension(0, 40)));
        box.add(btnUser);
        box.add(Box.createRigidArea(new Dimension(0, 15)));
        box.add(btnAdmin);

        panel.add(box);
        return panel;
    }

    private JPanel createUserBookingView() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(COLOR_BG);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(COLOR_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("Available Appointment Slots");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_PRIMARY);

        JButton back = new JButton("Home");
        back.addActionListener(e -> cardLayout.show(cardPanel, "START"));

        header.add(title, BorderLayout.WEST);
        header.add(back, BorderLayout.EAST);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(COLOR_BG);
        statsPanel.setBorder(new EmptyBorder(10, 20, 5, 20));

        totalSlotsLabel = createStatsCard(statsPanel, "Total Slots", COLOR_PRIMARY);
        availableSlotsLabel = createStatsCard(statsPanel, "Available Slots", COLOR_LOW);
        fullSlotsLabel = createStatsCard(statsPanel, "Full Slots", COLOR_FULL);

        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        legendPanel.setBackground(COLOR_BG);
        legendPanel.setBorder(new EmptyBorder(5, 20, 5, 20));

        legendPanel.add(createLegendItem(COLOR_LOW, "Low Booking"));
        legendPanel.add(createLegendItem(COLOR_MEDIUM, "Medium Booking"));
        legendPanel.add(createLegendItem(COLOR_HIGH, "Almost Full"));
        legendPanel.add(createLegendItem(COLOR_FULL, "Full"));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        filterPanel.setBackground(COLOR_BG);
        filterPanel.setBorder(new EmptyBorder(0, 20, 10, 20));

        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        filterComboBox = new JComboBox<>(new String[]{
                "All",
                "Low Booking",
                "Medium Booking",
                "Almost Full",
                "Full"
        });
        filterComboBox.addActionListener(e -> refreshUserSlots());

        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);

        JPanel northWrapper = new JPanel();
        northWrapper.setLayout(new BoxLayout(northWrapper, BoxLayout.Y_AXIS));
        northWrapper.setBackground(COLOR_BG);
        northWrapper.add(header);
        northWrapper.add(statsPanel);
        northWrapper.add(legendPanel);
        northWrapper.add(filterPanel);

        userGridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        userGridPanel.setBackground(COLOR_BG);
        userGridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        refreshUserSlots();

        main.add(northWrapper, BorderLayout.NORTH);
        main.add(new JScrollPane(userGridPanel), BorderLayout.CENTER);
        return main;
    }

    private JLabel createStatsCard(JPanel parent, String title, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel("0");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(valueLabel);

        parent.add(card);
        return valueLabel;
    }

    private JPanel createLegendItem(Color color, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);

        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(18, 18));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panel.add(colorBox);
        panel.add(label);

        return panel;
    }

    private void refreshUserSlots() {
        if (userGridPanel == null) {
            return;
        }

        userGridPanel.removeAll();

        List<AppointmentSlot> allSlots = appointmentService.getAvailableSlots();

        int totalSlots = allSlots.size();
        int fullSlots = 0;
        int availableSlots = 0;

        String selectedFilter = filterComboBox != null ? (String) filterComboBox.getSelectedItem() : "All";
        int displayedCount = 0;

        for (AppointmentSlot slot : allSlots) {
            if (slot.isFull()) {
                fullSlots++;
            } else {
                availableSlots++;
            }

            if (matchesFilter(slot, selectedFilter)) {
                userGridPanel.add(createSlotCard(slot));
                displayedCount++;
            }
        }

        if (displayedCount == 0) {
            userGridPanel.setLayout(new BorderLayout());
            JLabel emptyLabel = new JLabel("No slots match the selected filter.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            userGridPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            userGridPanel.setLayout(new GridLayout(0, 3, 20, 20));
        }

        updateStats(totalSlots, availableSlots, fullSlots);

        userGridPanel.revalidate();
        userGridPanel.repaint();
    }

    private boolean matchesFilter(AppointmentSlot slot, String filter) {
        if (filter == null || "All".equals(filter)) {
            return true;
        }
        return getSlotStatusText(slot).equals(filter);
    }

    private void updateStats(int total, int available, int full) {
        if (totalSlotsLabel != null) {
            totalSlotsLabel.setText(String.valueOf(total));
        }
        if (availableSlotsLabel != null) {
            availableSlotsLabel.setText(String.valueOf(available));
        }
        if (fullSlotsLabel != null) {
            fullSlotsLabel.setText(String.valueOf(full));
        }
    }

    private JPanel createSlotCard(AppointmentSlot slot) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        Color indicatorColor = getSlotIndicatorColor(slot);
        String statusText = getSlotStatusText(slot);

        JPanel indicatorBar = new JPanel();
        indicatorBar.setBackground(indicatorColor);
        indicatorBar.setPreferredSize(new Dimension(0, 12));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        JLabel timeLbl = new JLabel(slot.getStartTime().format(formatter));
        timeLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timeLbl.setForeground(COLOR_PRIMARY);
        timeLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel durationLbl = new JLabel("Duration: " + slot.getDurationMinutes() + " min");
        durationLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        durationLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel capacityLbl = new JLabel("Booked: " + slot.getBookedParticipants() + "/" + slot.getMaxParticipants());
        capacityLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        capacityLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusLbl = new JLabel(statusText + " (" + slot.getBookingPercentage() + "%)");
        statusLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLbl.setForeground(indicatorColor);
        statusLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(slot.getBookingPercentage());
        progressBar.setStringPainted(true);
        progressBar.setString(slot.getBookingPercentage() + "%");
        progressBar.setForeground(indicatorColor);
        progressBar.setBackground(new Color(240, 240, 240));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton book = new JButton(slot.isFull() ? "Full" : "Book");
        book.setBackground(slot.isFull() ? COLOR_FULL : COLOR_ACCENT);
        book.setForeground(Color.WHITE);
        book.setEnabled(!slot.isFull());

        book.addActionListener(e -> openBookingDialog(slot));

        contentPanel.add(timeLbl);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(durationLbl);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(capacityLbl);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(statusLbl);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(progressBar);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        contentPanel.add(book);

        card.add(indicatorBar, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private void openBookingDialog(AppointmentSlot slot) {
        JTextField usernameField = new JTextField();

        JComboBox<AppointmentType> typeCombo = new JComboBox<>(AppointmentType.values());
        typeCombo.setSelectedItem(AppointmentType.IN_PERSON);

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Appointment Type:"));
        panel.add(typeCombo);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Book Appointment",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String username = usernameField.getText();
        AppointmentType selectedType = (AppointmentType) typeCombo.getSelectedItem();

        try {
            boolean booked = appointmentService.bookAppointment(username, slot, selectedType);

            if (booked) {
                JOptionPane.showMessageDialog(this, "Appointment booked successfully.");
                refreshUserSlots();
                refreshAdminTable();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Booking failed. Slot may be full or already booked by this user.",
                        "Booking Failed",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Invalid Booking",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Color getSlotIndicatorColor(AppointmentSlot slot) {
        if (slot.isFull()) {
            return COLOR_FULL;
        }

        int percentage = slot.getBookingPercentage();

        if (percentage < 50) {
            return COLOR_LOW;
        } else if (percentage < 80) {
            return COLOR_MEDIUM;
        } else {
            return COLOR_HIGH;
        }
    }

    private String getSlotStatusText(AppointmentSlot slot) {
        if (slot.isFull()) {
            return "Full";
        }

        int percentage = slot.getBookingPercentage();

        if (percentage < 50) {
            return "Low Booking";
        } else if (percentage < 80) {
            return "Medium Booking";
        } else {
            return "Almost Full";
        }
    }

    private JPanel createAdminLoginView() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(40, 40, 40, 40)
        ));
        card.setBackground(Color.WHITE);

        JTextField usernameField = new JTextField(15);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton loginBtn = createStyledButton("Login", COLOR_PRIMARY);

        loginBtn.addActionListener(e -> {
            boolean success = authService.login(
                    usernameField.getText(),
                    new String(passwordField.getPassword())
            );

            if (success) {
                refreshAdminTable();
                cardLayout.show(cardPanel, "ADMIN_DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid credentials.",
                        "Authentication Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "START"));

        JLabel title = new JLabel("ADMIN AUTHENTICATION");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(usernameField);
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(loginBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(backBtn);

        panel.add(card);
        return panel;
    }

    private JPanel createAdminDashboard() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(COLOR_BG);

        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(COLOR_PRIMARY);
        head.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("Administration Dashboard");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            authService.logout();
            cardLayout.show(cardPanel, "START");
        });

        head.add(title, BorderLayout.WEST);
        head.add(logout, BorderLayout.EAST);

        String[] cols = {"User", "Type", "Date/Time", "Duration", "Booked", "Capacity", "Status"};
        adminTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        adminTable = new JTable(adminTableModel);
        adminTable.setRowHeight(28);
        adminTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        adminTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        adminTable.setDefaultRenderer(Object.class, new BookingStatusTableRenderer());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(COLOR_BG);

        JButton refreshBtn = new JButton("Refresh");
        JButton cancelBtn = new JButton("Cancel Selected");
        JButton modifyBtn = new JButton("Modify Selected");

        refreshBtn.addActionListener(e -> refreshAdminTable());
        cancelBtn.addActionListener(e -> cancelSelectedReservation());
        modifyBtn.addActionListener(e -> modifySelectedReservation());

        actionPanel.add(refreshBtn);
        actionPanel.add(cancelBtn);
        actionPanel.add(modifyBtn);

        main.add(head, BorderLayout.NORTH);
        main.add(new JScrollPane(adminTable), BorderLayout.CENTER);
        main.add(actionPanel, BorderLayout.SOUTH);

        return main;
    }

    private void refreshAdminTable() {
        if (adminTableModel == null) {
            return;
        }

        adminTableModel.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Appointment appointment : appointmentService.getAllAppointments()) {
            AppointmentSlot slot = appointment.getSlot();

            adminTableModel.addRow(new Object[]{
                    appointment.getUsername(),
                    appointment.getType(),
                    slot.getStartTime().format(formatter),
                    slot.getDurationMinutes() + " min",
                    slot.getBookedParticipants(),
                    slot.getBookedParticipants() + "/" + slot.getMaxParticipants(),
                    getSlotStatusText(slot)
            });
        }
    }

    private void cancelSelectedReservation() {
        int row = adminTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a reservation first.");
            return;
        }

        Appointment appointment = appointmentService.getAllAppointments().get(row);

        try {
            boolean cancelled = adminReservationService.cancelReservation(
                    appointment.getUsername(),
                    appointment.getSlot()
            );

            if (cancelled) {
                JOptionPane.showMessageDialog(this, "Reservation cancelled successfully.");
                refreshAdminTable();
                refreshUserSlots();
            } else {
                JOptionPane.showMessageDialog(this, "Unable to cancel this reservation.");
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Access Denied", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifySelectedReservation() {
        int row = adminTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a reservation first.");
            return;
        }

        Appointment appointment = appointmentService.getAllAppointments().get(row);
        List<AppointmentSlot> availableSlots = appointmentService.getAvailableSlots();

        DefaultComboBoxModel<SlotItem> model = new DefaultComboBoxModel<>();
        for (AppointmentSlot slot : availableSlots) {
            if (!slot.equals(appointment.getSlot())) {
                model.addElement(new SlotItem(slot));
            }
        }

        if (model.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No alternative slots available.");
            return;
        }

        JComboBox<SlotItem> slotCombo = new JComboBox<>(model);

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.add(new JLabel("Select new slot:"));
        panel.add(slotCombo);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Modify Reservation",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        SlotItem selected = (SlotItem) slotCombo.getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            boolean modified = adminReservationService.modifyReservation(
                    appointment.getUsername(),
                    appointment.getSlot(),
                    selected.getSlot()
            );

            if (modified) {
                JOptionPane.showMessageDialog(this, "Reservation modified successfully.");
                refreshAdminTable();
                refreshUserSlots();
            } else {
                JOptionPane.showMessageDialog(this, "Unable to modify this reservation.");
            }
        } catch (IllegalStateException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Modification Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 45));
        return button;
    }

    private class BookingStatusTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status = table.getValueAt(row, 6).toString();

            if (!isSelected) {
                if ("Low Booking".equals(status)) {
                    component.setBackground(new Color(220, 252, 231));
                } else if ("Medium Booking".equals(status)) {
                    component.setBackground(new Color(254, 249, 195));
                } else if ("Almost Full".equals(status)) {
                    component.setBackground(new Color(254, 226, 226));
                } else if ("Full".equals(status)) {
                    component.setBackground(new Color(229, 231, 235));
                } else {
                    component.setBackground(Color.WHITE);
                }
            }

            return component;
        }
    }

    private static class SlotItem {
        private final AppointmentSlot slot;

        public SlotItem(AppointmentSlot slot) {
            this.slot = slot;
        }

        public AppointmentSlot getSlot() {
            return slot;
        }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return slot.getStartTime().format(formatter)
                    + " | " + slot.getDurationMinutes() + " min"
                    + " | capacity " + slot.getBookedParticipants() + "/" + slot.getMaxParticipants();
        }
    }

    public static void main(String[] args) {
        AppointmentRepository repo = new AppointmentRepository();
        UserRepository userRepo = new UserRepository();
        AppointmentService appointmentService = new AppointmentService(repo);
        AuthenticationService authService = new AuthenticationService(userRepo);
        AdminReservationService adminReservationService =
                new AdminReservationService(authService, appointmentService);

        seedSampleSlots(repo);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() ->
                new MainGUI(authService, appointmentService, adminReservationService));
    }

    private static void seedSampleSlots(AppointmentRepository repo) {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withMinute(0).withSecond(0).withNano(0);

        AppointmentSlot slot1 = new AppointmentSlot(tomorrow.withHour(9), 60, 4);
        AppointmentSlot slot2 = new AppointmentSlot(tomorrow.withHour(10), 60, 4);
        AppointmentSlot slot3 = new AppointmentSlot(tomorrow.withHour(11), 90, 5);
        AppointmentSlot slot4 = new AppointmentSlot(tomorrow.withHour(13), 120, 3);
        AppointmentSlot slot5 = new AppointmentSlot(tomorrow.withHour(15), 30, 2);
        AppointmentSlot slot6 = new AppointmentSlot(tomorrow.withHour(16), 45, 2);

        slot1.addParticipant();
        slot2.addParticipant();
        slot2.addParticipant();
        slot3.addParticipant();
        slot3.addParticipant();
        slot3.addParticipant();
        slot3.addParticipant();
        slot4.addParticipant();
        slot4.addParticipant();
        slot5.addParticipant();
        slot5.addParticipant();

        repo.addSlot(slot1);
        repo.addSlot(slot2);
        repo.addSlot(slot3);
        repo.addSlot(slot4);
        repo.addSlot(slot5);
        repo.addSlot(slot6);
    }
}