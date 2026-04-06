import java.util.*;

// --- Custom Exceptions for Domain-Specific Errors ---
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) { super(message); }
}

class InsufficientInventoryException extends Exception {
    public InsufficientInventoryException(String message) { super(message); }
}

public class UseCase9ErrorHandlingValidation {

    private Map<String, Integer> inventory = new HashMap<>();
    private Set<String> validRoomTypes = new HashSet<>(Arrays.asList("DELUXE", "SUITE"));

    public UseCase9ErrorHandlingValidation() {
        inventory.put("DELUXE", 1); // Only 1 deluxe available for testing
        inventory.put("SUITE", 5);
    }

    /**
     * Processes a booking with Fail-Fast Validation.
     * Note: It is case-sensitive as per requirements.
     */
    public void processBooking(String guestName, String roomType) throws InvalidRoomTypeException, InsufficientInventoryException {

        // 1. Validate Input (Room Type existence)
        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidRoomTypeException("FAILED: Room type '" + roomType + "' is not supported by this hotel.");
        }

        // 2. Validate System State (Inventory availability)
        int currentStock = inventory.getOrDefault(roomType, 0);
        if (currentStock <= 0) {
            throw new InsufficientInventoryException("FAILED: No " + roomType + " rooms available for guest " + guestName + ".");
        }

        // 3. Update State only if all validations pass
        inventory.put(roomType, currentStock - 1);
        System.out.println("SUCCESS: Room allocated for " + guestName + " (" + roomType + ")");
    }

    public static void main(String[] args) {
        UseCase9ErrorHandlingValidation service = new UseCase9ErrorHandlingValidation();

        // Test Scenarios
        String[][] testRequests = {
                {"Alice", "DELUXE"},   // Should succeed
                {"Bob", "DELUXE"},     // Should fail (Insufficient Inventory)
                {"Charlie", "PENTHOUSE"}, // Should fail (Invalid Room Type)
                {"David", "suite"}     // Should fail (Case-sensitive check)
        };

        for (String[] request : testRequests) {
            String name = request[0];
            String type = request[1];

            try {
                service.processBooking(name, type);
            } catch (InvalidRoomTypeException | InsufficientInventoryException e) {
                // Graceful failure handling - system stays running
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }

        System.out.println("\n--- Final System Status: App is still running safely ---");
        System.out.println("Remaining Inventory: " + service.inventory);
    }
}