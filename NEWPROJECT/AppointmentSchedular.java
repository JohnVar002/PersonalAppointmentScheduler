/*Personal Appointment Schedular
Members:
Keir John Variacion - Project Manager/Programmer/Technical Writer
John Leo Flores - Programmer/ Flowchart
Kesha Crabajales - Technical Writer
Selwyn Noel Pil - Flowchart
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

class User {
    String name;
    String lastName;
    String email;
    String password;

    public User(String name, String lastName, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}

class Appointment {
    String title;
    String place;
    String time;
    String date;
    String description;
    String userEmail;

    public Appointment(String title, String place, String time, String date, String description, String userEmail) {
        this.title = title;
        this.place = place;
        this.time = time;
        this.date = date;
        this.description = description;
        this.userEmail = userEmail;
    }
}

public class AppointmentScheduler {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Appointment> appointments = new ArrayList<>();
    private static User currentUser = null;
    private static String APPOINTMENTS_FILE_FORMAT = "%s_appointments.txt";
    private static String USERS_FILE = "users.txt";

    public static void main(String[] args) {
        loadUsers();
        loadAppointments();

        System.out.println("╔════════════════════╗");
        System.out.println("║     REGISTERED?    ║");
        System.out.println("╚════════════════════╝");

        int registeredChoice = getRegisteredChoice();

        if (registeredChoice == 1) {
            login();
        } else if (registeredChoice == 2) {
            createAccount();
        } else if (registeredChoice == 3) {
            System.out.println("Exiting.");
            System.exit(0);
        } else {
            System.out.println("Invalid choice. Exiting.");
            System.exit(0);
        }

        login();
    }

    private static int getRegisteredChoice() {
        while (true) {
            System.out.println("1. YES\n2. NO\n3.EXIT");
            String choice = scanner.nextLine();

            try {
                int registeredChoice = Integer.parseInt(choice);
                if (registeredChoice == 1 || registeredChoice == 2 || registeredChoice == 3) {
                    return registeredChoice;
                } else {
                    System.out.println("Invalid choice. Please choose 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void login() {
        System.out.println("╔════════════════════╗");
        System.out.println("║       LOGIN        ║");
        System.out.println("╚════════════════════╝");
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        loadUsers();

        for (User user : users) {
            if (user.email.equals(email) && user.password.equals(password)) {
                currentUser = user;
                System.out.println("Login successful. Welcome, " + user.name + "!");
                mainMenu();
                return;
            }
        }

        System.out.println("Invalid credentials or user not registered.");
        System.out.println("1. Try creating an account\n2. Go back to REGISTERED");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            createAccount();
        } else if (choice == 2) {
            login();
        } else {
            main(null);
        }
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("╔════════════════════╗");
            System.out.println("║      MAIN MENU     ║");
            System.out.println("╚════════════════════╝");
            System.out.println("1. Create Appointment\n2. View Appointments\n3. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createAppointment();
                    break;
                case 2:
                    viewAppointments();
                    break;
                case 3:
                    currentUser = null;
                    returnToRegistered();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void returnToRegistered() {
        System.out.println("Returning to REGISTERED...");
        main(null);
    }

    private static void createAccount() {
        System.out.println("╔════════════════════╗");
        System.out.println("║   CREATE ACCOUNT   ║");
        System.out.println("╚════════════════════╝");
        System.out.println("Enter your first name:");
        String name = scanner.nextLine();
        System.out.println("Enter your last name:");
        String lastName = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();

        for (User user : users) {
            if (user.email.equals(email)) {
                System.out.println("Email is already registered. Please choose a different email.");
                return;
            }
        }

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        User newUser = new User(name, lastName, email, password);
        users.add(newUser);

        System.out.println("Account created successfully. You can now log in.");
        saveUser(newUser);
    }

    private static void createAppointment() {
        if (currentUser == null) {
            System.out.println("You need to log in to create an appointment.");
            return;
        }

        System.out.println("╔════════════════════╗");
        System.out.println("║ CREATE APPOINTMENT ║");
        System.out.println("╚════════════════════╝");

        // Validation for each form field
        String title = getNonEmptyInput("Enter title:");
        String place = getNonEmptyInput("Enter place:");
        String time = getNonEmptyInput("Enter time:");
        String date = getNonEmptyInput("Enter date:");
        String description = getNonEmptyInput("Enter description:");

        Appointment newAppointment = new Appointment(title, place, time, date, description, currentUser.email);
        appointments.add(newAppointment);

        System.out.println("Appointment created successfully.");
        saveAppointment(newAppointment, currentUser.email);
    }

    private static void viewAppointments() {
        loadAppointments();

        while (true) {
            System.out.println("╔════════════════════╗");
            System.out.println("║  VIEW APPOINTMENT  ║");
            System.out.println("╚════════════════════╝");

            for (Appointment appointment : appointments) {
                if (appointment.userEmail.equals(currentUser.email)) {
                    System.out.println("Title: " + appointment.title);
                    System.out.println("Place: " + appointment.place);
                    System.out.println("Time: " + appointment.time);
                    System.out.println("Date: " + appointment.date);
                    System.out.println("Description: " + appointment.description);
                    System.out.println("----------------------");
                }
            }

            System.out.println("1. Delete Appointment\n2. Prioritize Appointment\n3. Back to Main Menu");
            String viewChoice = scanner.nextLine();

            try {
                int choice = Integer.parseInt(viewChoice);
                switch (choice) {
                    case 1:
                        deleteAppointment();
                        break;
                    case 2:
                        prioritizeAppointment();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void deleteAppointment() {
        System.out.println("Enter the title of the appointment to delete:");
        String titleToDelete = scanner.nextLine();

        for (Appointment appointment : appointments) {
            if (appointment.title.equals(titleToDelete) && appointment.userEmail.equals(currentUser.email)) {
                appointments.remove(appointment);
                saveUserAppointments(currentUser.email);
                System.out.println("Appointment deleted successfully.");
                return;
            }
        }

        System.out.println("Appointment not found.");
    }

    private static void prioritizeAppointment() {
        System.out.println("Enter the title of the appointment to prioritize:");
        String titleToPrioritize = scanner.nextLine();

        for (Appointment appointment : appointments) {
            if (appointment.title.equals(titleToPrioritize) && appointment.userEmail.equals(currentUser.email)) {
                appointments.remove(appointment);
                appointments.add(0, appointment);
                saveUserAppointments(currentUser.email);
                System.out.println("Appointment prioritized successfully.");
                return;
            }
        }

        System.out.println("Appointment not found.");
    }

    private static void saveUser(User user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE, true))) {
            writer.println(user.name + "," + user.lastName + "," + user.email + "," + user.password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveAppointment(Appointment appointment, String userEmail) {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(String.format(APPOINTMENTS_FILE_FORMAT, userEmail), true))) {
            writer.println(appointment.title + "," + appointment.place + "," + appointment.time + ","
                    + appointment.date + "," + appointment.description);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveUserAppointments(String userEmail) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(String.format(APPOINTMENTS_FILE_FORMAT, userEmail)))) {
            for (Appointment appointment : appointments) {
                if (appointment.userEmail.equals(userEmail)) {
                    writer.println(appointment.title + "," + appointment.place + "," + appointment.time + ","
                            + appointment.date + "," + appointment.description);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadAppointments() {
        if (currentUser != null) {
            appointments.clear();

            try (BufferedReader reader = new BufferedReader(
                    new FileReader(String.format(APPOINTMENTS_FILE_FORMAT, currentUser.email)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        Appointment appointment = new Appointment(parts[0], parts[1], parts[2], parts[3], parts[4],
                                currentUser.email);
                        appointments.add(appointment);
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    private static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    User user = new User(parts[0], parts[1], parts[2], parts[3]);
                    users.add(user);
                }
            }
        } catch (IOException e) {
        }
    }
}
