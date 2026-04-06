import java.util.*;

class UseCase10BookingCancellation {

    // Current Active Bookings: ReservationID -> RoomID
    private Map<String, String> activeBookings = new HashMap<>();

    // Inventory: RoomType -> Count
    private Map<String, Integer> inventory = new HashMap<>();

    // Rollback Structure: Stack to track released Room IDs (LIFO)
    private Stack<String> releasedRoomsStack = new Stack<>();

    public UseCase10BookingCancellation() {
        // Initial State
        inventory.put("DELUXE", 0); // All booked
        activeBookings.put("RES-101", "DELUXE-101");
        activeBookings.put("RES-102", "DELUXE-102");
    }

    /**
     * Performs a controlled rollback of a booking.
     */
    public void cancelBooking(String reservationId, String roomType) {
        System.out.println("Attempting cancellation for: " + reservationId);

        // 1. Validation: Ensure the reservation exists
        if (!activeBookings.containsKey(reservationId)) {
            System.out.println("ERROR: Cancellation Failed. Reservation " + reservationId + " not found.");
            return;
        }

        // 2. Identify the Room to be released
        String roomId = activeBookings.remove(reservationId);

        // 3. State Reversal: Push Room ID to Stack (LIFO Rollback)
        releasedRoomsStack.push(roomId);

        // 4. Inventory Restoration: Increment count immediately
        inventory.put(roomType, inventory.get(roomType) + 1);

        System.out.println("SUCCESS: " + reservationId + " cancelled. Room " + roomId + " returned to pool.");
    }

    public void displaySystemState() {
        System.out.println("\n--- Current System State ---");
        System.out.println("Active Bookings: " + activeBookings);
        System.out.println("Inventory:       " + inventory);
        System.out.println("Released Stack:  " + releasedRoomsStack);
        System.out.println("----------------------------\n");
    }

    public static void main(String[] args) {
        UseCase10BookingCancellation service = new UseCase10BookingCancellation();

        service.displaySystemState();

        // Guest initiates cancellation
        service.cancelBooking("RES-102", "DELUXE");
        service.cancelBooking("RES-101", "DELUXE");

        // Attempting to cancel a non-existent booking (Validation check)
        service.cancelBooking("RES-999", "DELUXE");

        service.displaySystemState();

        // Demonstrating LIFO: The last room cancelled (101) is at the top of the stack
        if (!service.releasedRoomsStack.isEmpty()) {
            System.out.println("Next room available for priority re-assignment: " + service.releasedRoomsStack.peek());
        }
    }
}