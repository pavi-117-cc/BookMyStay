/**
 * UseCase4RoomSearch
 *
 * Demonstrates read-only room search functionality using
 * centralized inventory and domain models.
 * Ensures no modification to system state during search.
 *
 * @author YourName
 * @version 4.0
 */

import java.util.*;

// -------------------- Domain Model --------------------
abstract class Room {
    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayDetails() {
        System.out.println("Room Type  : " + roomType);
        System.out.println("Beds       : " + beds);
        System.out.println("Price      : $" + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 1800);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 3000);
    }
}

// -------------------- Inventory (Read Only Usage) --------------------
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0); // intentionally unavailable
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Set<String> getAllRoomTypes() {
        return inventory.keySet();
    }
}

// -------------------- Search Service --------------------
class RoomSearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;

        // Initialize room catalog (domain objects)
        roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom());
        roomCatalog.put("Double Room", new DoubleRoom());
        roomCatalog.put("Suite Room", new SuiteRoom());
    }

    /**
     * Read-only search operation
     */
    public void searchAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");

        for (String type : inventory.getAllRoomTypes()) {

            int available = inventory.getAvailability(type);

            // Defensive check: only show available rooms
            if (available > 0) {
                Room room = roomCatalog.get(type);

                room.displayDetails();
                System.out.println("Available : " + available);
                System.out.println("----------------------------");
            }
        }
    }
}

// -------------------- Main Class --------------------
class UseCase4RoomSearch {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" Welcome to Book My Stay Application ");
        System.out.println(" Version: 4.0 ");
        System.out.println("======================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Perform search (read-only)
        searchService.searchAvailableRooms();
    }
}