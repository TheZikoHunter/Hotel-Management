package com.code.hetelview.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for Employee model class.
 */
@DisplayName("Tests du modèle Employee")
class EmployeeTest {
    
    private Employee employee;
    
    @BeforeEach
    void setUp() {
        employee = new Employee();
    }
    
    @Test
    @DisplayName("Devrait créer un employé avec le constructeur par défaut")
    void shouldCreateEmployeeWithDefaultConstructor() {
        // Given & When
        Employee emp = new Employee();
        
        // Then
        assertThat(emp).isNotNull();
        assertThat(emp.getId()).isEqualTo(0);
        assertThat(emp.getUsername()).isNull();
        assertThat(emp.getPassword()).isNull();
        assertThat(emp.getFullName()).isNull();
        assertThat(emp.getRole()).isNull();
    }
    
    @Test
    @DisplayName("Devrait créer un employé avec le constructeur paramétré")
    void shouldCreateEmployeeWithParameterizedConstructor() {
        // Given & When
        Employee emp = new Employee(1, "testuser", "password123", "Test User", "réceptionniste");
        
        // Then
        assertThat(emp.getId()).isEqualTo(1);
        assertThat(emp.getUsername()).isEqualTo("testuser");
        assertThat(emp.getPassword()).isEqualTo("password123");
        assertThat(emp.getFullName()).isEqualTo("Test User");
        assertThat(emp.getRole()).isEqualTo("réceptionniste");
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer l'ID correctement")
    void shouldSetAndGetIdCorrectly() {
        // Given
        int expectedId = 42;
        
        // When
        employee.setId(expectedId);
        
        // Then
        assertThat(employee.getId()).isEqualTo(expectedId);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer le nom d'utilisateur correctement")
    void shouldSetAndGetUsernameCorrectly() {
        // Given
        String expectedUsername = "marie.leclerc";
        
        // When
        employee.setUsername(expectedUsername);
        
        // Then
        assertThat(employee.getUsername()).isEqualTo(expectedUsername);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer le mot de passe correctement")
    void shouldSetAndGetPasswordCorrectly() {
        // Given
        String expectedPassword = "hashedPassword123";
        
        // When
        employee.setPassword(expectedPassword);
        
        // Then
        assertThat(employee.getPassword()).isEqualTo(expectedPassword);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer le nom complet correctement")
    void shouldSetAndGetFullNameCorrectly() {
        // Given
        String expectedFullName = "Marie Leclerc";
        
        // When
        employee.setFullName(expectedFullName);
        
        // Then
        assertThat(employee.getFullName()).isEqualTo(expectedFullName);
    }
    
    @Test
    @DisplayName("Devrait définir et récupérer le rôle correctement")
    void shouldSetAndGetRoleCorrectly() {
        // Given
        String expectedRole = "réceptionniste";
        
        // When
        employee.setRole(expectedRole);
        
        // Then
        assertThat(employee.getRole()).isEqualTo(expectedRole);
    }
    
    @Test
    @DisplayName("Devrait supporter tous les rôles d'hôtel")
    void shouldSupportAllHotelRoles() {
        // Given
        String[] hotelRoles = {
            "admin", "chef de réception", "réceptionniste", "standardiste",
            "voiturier", "concierge", "agent de sécurité", "bagagiste",
            "guide", "caissier", "valet de chambre", "femme de chambre",
            "gouvernante", "serveur d'étage", "lingère"
        };
        
        // When & Then
        for (String role : hotelRoles) {
            employee.setRole(role);
            assertThat(employee.getRole()).isEqualTo(role);
        }
    }
    
    @Test
    @DisplayName("Devrait générer toString correctement")
    void shouldGenerateToStringCorrectly() {
        // Given
        employee.setId(1);
        employee.setUsername("testuser");
        employee.setFullName("Test User");
        employee.setRole("admin");
        
        // When
        String result = employee.toString();
        
        // Then
        assertThat(result)
            .contains("Employee{")
            .contains("id=1")
            .contains("username='testuser'")
            .contains("fullName='Test User'")
            .contains("role='admin'")
            .doesNotContain("password"); // Le mot de passe ne doit pas apparaître dans toString
    }
    
    @Test
    @DisplayName("Devrait gérer les valeurs nulles gracieusement")
    void shouldHandleNullValuesGracefully() {
        // When
        employee.setUsername(null);
        employee.setPassword(null);
        employee.setFullName(null);
        employee.setRole(null);
        
        // Then
        assertThat(employee.getUsername()).isNull();
        assertThat(employee.getPassword()).isNull();
        assertThat(employee.getFullName()).isNull();
        assertThat(employee.getRole()).isNull();
        
        // ToString ne doit pas lever d'exception avec des valeurs nulles
        assertThatCode(() -> employee.toString()).doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("Devrait gérer les chaînes vides")
    void shouldHandleEmptyStrings() {
        // When
        employee.setUsername("");
        employee.setPassword("");
        employee.setFullName("");
        employee.setRole("");
        
        // Then
        assertThat(employee.getUsername()).isEmpty();
        assertThat(employee.getPassword()).isEmpty();
        assertThat(employee.getFullName()).isEmpty();
        assertThat(employee.getRole()).isEmpty();
    }
}
