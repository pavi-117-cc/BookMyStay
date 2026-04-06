import java.util.*;

class UseCase11ConcurrentBookingSimulation {

    // Shared Resources
    private int inventory = 3; // Only 3 rooms available
    private final List<String> confirmedBookings = new ArrayList<>();
    private final Object lock = new Object(); // Explicit lock object for synchronization

    /**
     * Attempts to book a room.
     * The 'synchronized' block ensures this is a Critical Section.
     */
    public void bookRoom(String guestName) {
        System.out.println(guestName + " is attempting to book...");

        // Synchronize on the lock object to prevent Race Conditions
        synchronized (lock) {
            if (inventory > 0) {
                // Simulate some processing time to increase the chance of a race condition
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }

                inventory--;
                confirmedBookings.add(guestName);
                System.out.println("SUCCESS: Room allocated to " + guestName + ". Remaining: " + inventory);
            } else {
                System.out.println("FAILED: No rooms left for " + guestName);
            }
        }
    }

    public void displayResults() {
        System.out.println("\n--- Final Simulation Results ---");
        System.out.println("Total Inventory Remaining: " + inventory);
        System.out.println("Confirmed Guests: " + confirmedBookings);
        System.out.println("Total Bookings: " + confirmedBookings.size());
    }

    public static void main(String[] args) {
        UseCase11ConcurrentBookingSimulation simulation = new UseCase11ConcurrentBookingSimulation();

        // Create a pool of threads representing concurrent guests
        Thread t1 = new Thread(() -> simulation.bookRoom("Guest-Alice"));
        Thread t2 = new Thread(() -> simulation.bookRoom("Guest-Bob"));
        Thread t3 = new Thread(() -> simulation.bookRoom("Guest-Charlie"));
        Thread t4 = new Thread(() -> simulation.bookRoom("Guest-David"));
        Thread t5 = new Thread(() -> simulation.bookRoom("Guest-Eve"));

        // Start all threads simultaneously
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        // Wait for all threads to finish (Join)
        try {
            t1.join(); t2.join(); t3.join(); t4.join(); t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        simulation.displayResults();
    }
}