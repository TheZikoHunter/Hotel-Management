package com.code.hetelview.test;

/**
 * Simple test to verify role-based navigation visibility
 */
public class NavigationTest {
    
    public static void main(String[] args) {
        // Test roles that should see employee management
        String[] authorizedRoles = {"admin", "chef de réception", "gouvernante"};
        String[] unauthorizedRoles = {"réceptionniste", "concierge", "femme de chambre"};
        
        System.out.println("=== Navigation Visibility Test ===");
        
        System.out.println("\nRoles that SHOULD see employee management:");
        for (String role : authorizedRoles) {
            boolean canSee = canSeeEmployeeManagement(role);
            System.out.println("  " + role + ": " + (canSee ? "✓ CAN SEE" : "✗ CANNOT SEE"));
        }
        
        System.out.println("\nRoles that should NOT see employee management:");
        for (String role : unauthorizedRoles) {
            boolean canSee = canSeeEmployeeManagement(role);
            System.out.println("  " + role + ": " + (canSee ? "✗ CAN SEE (PROBLEM!)" : "✓ CANNOT SEE"));
        }
    }
    
    /**
     * Simulates the JSP condition for showing employee management button
     */
    private static boolean canSeeEmployeeManagement(String role) {
        return "admin".equals(role) || 
               "chef de réception".equals(role) || 
               "gouvernante".equals(role);
    }
}
