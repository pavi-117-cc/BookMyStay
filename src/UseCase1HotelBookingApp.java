import java.util.*;

// Class representing an optional service
class Service {
    String serviceName;
    double price;

    public Service(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    @Override
    public String toString() {
        return serviceName + " ($" + price + ")";
    }
}

class UseCase7AddOnServiceSelection {

    // One-to-Many Relationship: Reservation ID -> List of Services
    private Map<String, List<Service>> reservationAddOns = new HashMap<>();

    /**
     * Adds a service to a specific reservation.
     * Demonstrates how Map and List work together for extensibility.
     */
    public void addServiceToReservation(String reservationId, Service service) {
        // If the reservationId doesn't exist in the map, create a new list
        reservationAddOns.putIfAbsent(reservationId, new ArrayList<>());

        // Add the service to the list associated with this reservation
        reservationAddOns.get(reservationId).add(service);

        System.out.println("Service Added: " + service.serviceName + " to Reservation " + reservationId);
    }

    /**
     * Calculates the total cost of all add-ons for a specific reservation.
     */
    public double calculateTotalAddOnCost(String reservationId) {
        double total = 0.0;
        List<Service> services = reservationAddOns.get(reservationId);

        if (services != null) {
            for (Service s : services) {
                total += s.price;
            }
        }
        return total;
    }

    /**
     * Displays the summary of services for a guest.
     */
    public void displayReservationSummary(String reservationId) {
        System.out.println("\n--- Summary for Reservation: " + reservationId + " ---");
        List<Service> services = reservationAddOns.getOrDefault(reservationId, new ArrayList<>());

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
        } else {
            System.out.println("Selected Services: " + services);
            System.out.println("Total Add-On Cost: $" + calculateTotalAddOnCost(reservationId));
        }
    }

    public static void main(String[] args) {
        UseCase7AddOnServiceSelection manager = new UseCase7AddOnServiceSelection();

        // Define available services
        Service breakfast = new Service("Buffet Breakfast", 25.0);
        Service wifi = new Service("High-Speed WiFi", 10.0);
        Service spa = new Service("Spa Treatment", 120.0);
        Service laundry = new Service("Express Laundry", 30.0);

        // Scenario: Guest Alice (RES-101) wants breakfast and WiFi
        manager.addServiceToReservation("RES-101", breakfast);
        manager.addServiceToReservation("RES-101", wifi);

        // Scenario: Guest Bob (RES-102) wants a premium experience
        manager.addServiceToReservation("RES-102", breakfast);
        manager.addServiceToReservation("RES-102", spa);
        manager.addServiceToReservation("RES-102", laundry);

        // Output Results
        manager.displayReservationSummary("RES-101");
        manager.displayReservationSummary("RES-102");
        manager.displayReservationSummary("RES-103"); // Test empty case
    }
}