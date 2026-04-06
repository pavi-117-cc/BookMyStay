import java.io.*;
import java.util.*;

// The system state must implement Serializable to be saved to a file
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;
    Map<String, Integer> inventory;
    List<String> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<String> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

class UseCase12DataPersistenceRecovery {

    private static final String STORAGE_FILE = "hotel_state.dat";
    private Map<String, Integer> inventory = new HashMap<>();
    private List<String> bookingHistory = new ArrayList<>();

    public UseCase12DataPersistenceRecovery() {
        // Default initial state if no file exists
        inventory.put("DELUXE", 10);
        inventory.put("SUITE", 5);
    }

    /**
     * Persists the current in-memory state to a file (Serialization).
     */
    public void saveState() {
        System.out.println("--- Saving System State to Storage ---");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            SystemState state = new SystemState(inventory, bookingHistory);
            oos.writeObject(state);
            System.out.println("SUCCESS: State persisted to " + STORAGE_FILE);
        } catch (IOException e) {
            System.err.println("ERROR: Failed to save state: " + e.getMessage());
        }
    }

    /**
     * Restores the state from a file (Deserialization).
     */
    @SuppressWarnings("unchecked")
    public void loadState() {
        File file = new File(STORAGE_FILE);
        if (!file.exists()) {
            System.out.println("NOTICE: No persistence file found. Starting with fresh state.");
            return;
        }

        System.out.println("--- Recovering System State from Storage ---");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            SystemState restored = (SystemState) ois.readObject();
            this.inventory = restored.inventory;
            this.bookingHistory = restored.bookingHistory;
            System.out.println("SUCCESS: State recovered. Current Bookings: " + bookingHistory.size());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ERROR: Recovery failed. Starting with default state. " + e.getMessage());
        }
    }

    public void addBooking(String guest, String type) {
        if (inventory.getOrDefault(type, 0) > 0) {
            inventory.put(type, inventory.get(type) - 1);
            bookingHistory.add(guest + " (" + type + ")");
            System.out.println("Booking added: " + guest);
        }
    }

    public void displayState() {
        System.out.println("Current Inventory: " + inventory);
        System.out.println("Booking History:   " + bookingHistory);
        System.out.println("--------------------------------------");
    }

    public static void main(String[] args) {
        UseCase12DataPersistenceRecovery app = new UseCase12DataPersistenceRecovery();

        // 1. Try to recover from previous run
        app.loadState();
        app.displayState();

        // 2. Perform some operations
        System.out.println("Processing new bookings...");
        app.addBooking("Alice", "DELUXE");
        app.addBooking("Bob", "SUITE");

        app.displayState();

        // 3. Save state before "shutdown"
        app.saveState();
        System.out.println("Application Shutting Down.");
    }
}