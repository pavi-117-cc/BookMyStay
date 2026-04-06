import java.util.*;

class UseCase6RoomAllocationService {

    // Queue to store incoming booking requests (FIFO)
    private Queue<BookingRequest> bookingQueue = new LinkedList<>();

    // Mapping Room Types to a Set of uniquely assigned Room IDs
    // HashMap<RoomType, Set<AllocatedRoomIDs>>
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Simple inventory tracking
    private Map<String, Integer> inventory = new HashMap<>();

    public UseCase6RoomAllocationService() {
        // Initialize inventory
        inventory.put("DELUXE", 5);
        inventory.put("SUITE", 2);

        // Initialize the sets for each room type
        allocatedRooms.put("DELUXE", new HashSet<>());
        allocatedRooms.put("SUITE", new HashSet<>());
    }

    // Represents a booking request
    static class BookingRequest {
        String guestName;
        String roomType;

        public BookingRequest(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    public void addRequest(String name, String type) {
        bookingQueue.add(new BookingRequest(name, type));
    }

    public void processAllocations() {
        System.out.println("--- Starting Room Allocation Process ---");

        while (!bookingQueue.isEmpty()) {
            BookingRequest request = bookingQueue.poll();
            String type = request.roomType;

            // 1. Check Availability
            if (inventory.containsKey(type) && inventory.get(type) > 0) {

                // 2. Generate Unique Room ID
                // In a real system, this would pull from a list of physical rooms.
                // Here, we generate one based on current count + 100
                int roomNumber = 100 + (allocatedRooms.get(type).size() + 1);
                String generatedRoomId = type + "-" + roomNumber;

                // 3. Prevent Double-Booking (Set Uniqueness Enforcement)
                if (!allocatedRooms.get(type).contains(generatedRoomId)) {

                    // 4. Atomic Logical Operation: Add to Set and Decrement Inventory
                    allocatedRooms.get(type).add(generatedRoomId);
                    inventory.put(type, inventory.get(type) - 1);

                    System.out.println("CONFIRMED: " + request.guestName +
                            " assigned to " + generatedRoomId);
                } else {
                    System.out.println("ERROR: Room ID Collision detected for " + generatedRoomId);
                }
            } else {
                System.out.println("REJECTED: No availability for " + request.guestName + " (" + type + ")");
            }
        }
        System.out.println("--- Allocation Process Complete ---\n");
    }

    public void displayFinalState() {
        System.out.println("Final Inventory State: " + inventory);
        System.out.println("Allocated Rooms: " + allocatedRooms);
    }

    public static void main(String[] args) {
        UseCase6RoomAllocationService service = new UseCase6RoomAllocationService();

        // Adding requests to the queue
        service.addRequest("Alice", "DELUXE");
        service.addRequest("Bob", "DELUXE");
        service.addRequest("Charlie", "SUITE");
        service.addRequest("David", "SUITE");
        service.addRequest("Eve", "SUITE"); // Should be rejected (only 2 suites available)

        // Process the queue
        service.processAllocations();

        // Show results
        service.displayFinalState();
    }
}