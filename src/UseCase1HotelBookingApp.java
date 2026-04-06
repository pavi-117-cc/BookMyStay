import java.util.*;

// Represents a finalized reservation record for historical tracking
class Reservation {
    String reservationId;
    String guestName;
    String roomType;
    String roomId;
    double totalCost;

    public Reservation(String reservationId, String guestName, String roomType, String roomId, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return String.format("| %-8s | %-10s | %-8s | %-10s | $%-8.2f |",
                reservationId, guestName, roomType, roomId, totalCost);
    }
}

class UseCase8BookingHistoryReport {

    // The historical data store - maintains chronological order of confirmations
    private List<Reservation> bookingHistory = new ArrayList<>();

    /**
     * Adds a confirmed booking to the history log.
     * This simulates persistence.
     */
    public void recordBooking(Reservation res) {
        bookingHistory.add(res);
    }

    /**
     * Generates a detailed audit report of all transactions.
     */
    public void generateFullReport() {
        System.out.println("\n--- OFFICIAL BOOKING HISTORY REPORT ---");
        System.out.println("------------------------------------------------------------");
        System.out.println("| Res ID   | Guest      | Type     | Room ID    | Cost      |");
        System.out.println("------------------------------------------------------------");

        for (Reservation res : bookingHistory) {
            System.out.println(res);
        }

        System.out.println("------------------------------------------------------------");
    }

    /**
     * Generates a high-level summary report (Revenue and Volume).
     */
    public void generateSummaryReport() {
        double totalRevenue = 0;
        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation res : bookingHistory) {
            totalRevenue += res.totalCost;
            roomTypeCount.put(res.roomType, roomTypeCount.getOrDefault(res.roomType, 0) + 1);
        }

        System.out.println("\n--- EXECUTIVE SUMMARY ---");
        System.out.println("Total Bookings Processed: " + bookingHistory.size());
        System.out.println("Total Revenue Generated:  $" + totalRevenue);
        System.out.println("Bookings by Category:    " + roomTypeCount);
        System.out.println("--------------------------\n");
    }

    public static void main(String[] args) {
        UseCase8BookingHistoryReport reportService = new UseCase8BookingHistoryReport();

        // Simulating the transition from Use Case 6/7 to History
        // In a real app, these would be passed from the Allocation Service
        reportService.recordBooking(new Reservation("RSV001", "Alice", "DELUXE", "DELUXE-101", 185.0));
        reportService.recordBooking(new Reservation("RSV002", "Bob", "SUITE", "SUITE-201", 450.0));
        reportService.recordBooking(new Reservation("RSV003", "Charlie", "DELUXE", "DELUXE-102", 150.0));
        reportService.recordBooking(new Reservation("RSV004", "David", "SUITE", "SUITE-202", 520.0));

        // Admin requests reports
        reportService.generateFullReport();
        reportService.generateSummaryReport();
    }
}