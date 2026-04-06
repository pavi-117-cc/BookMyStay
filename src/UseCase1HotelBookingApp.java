/**
 * UseCase5BookingRequestQueue
 *
 * Demonstrates booking request handling using FIFO Queue.
 * Ensures fair ordering of requests without modifying inventory.
 *
 * @author YourName
 * @version 5.0
 */

import java.util.*;

// -------------------- Reservation Class --------------------
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room Type  : " + roomType);
    }
}

// -------------------- Booking Queue --------------------
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    /**
     * Add booking request to queue
     */
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    /**
     * View all queued requests (without removing)
     */
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue ---");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.displayReservation();
            System.out.println("--------------------------");
        }
    }

    /**
     * Get next request (for future processing)
     */
    public Reservation getNextRequest() {
        return queue.peek(); // does not remove
    }
}

// -------------------- Main Class --------------------
class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" Welcome to Book My Stay Application ");
        System.out.println(" Version: 5.0 ");
        System.out.println("======================================");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Display queue (FIFO order preserved)
        bookingQueue.displayQueue();

        // Show next request to be processed
        System.out.println("\nNext Request to Process:");
        Reservation next = bookingQueue.getNextRequest();
        if (next != null) {
            next.displayReservation();
        }
    }
}