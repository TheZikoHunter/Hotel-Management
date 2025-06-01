package com.code.hetelview.dao;

import com.code.hetelview.model.Employee;
import com.code.hetelview.util.HibernateTestUtil;
import com.code.hetelview.util.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import java.util.List;

/**
 * Tests d'intégration pour EmployeeDAO.
 * Utilise une base de données de test MySQL.
 */
@DisplayName("Tests d'intégration EmployeeDAO")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeDAOIntegrationTest {
    
    private EmployeeDAO employeeDAO;
    private Employee testEmployee;
    
    @BeforeAll
    void setUpDatabase() {
        // Créer les tables et insérer des données de test
        try (Session session = HibernateTestUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            
            // Les tables sont créées automatiquement par Hibernate avec create-drop
            // Insérer un employé de test
            Employee admin = new Employee();
            admin.setUsername("admin_test");
            admin.setPassword(PasswordUtil.hashPassword("password123"));
            admin.setFullName("Admin Test");
            admin.setRole("admin");
            session.save(admin);
            
            Employee receptionniste = new Employee();
            receptionniste.setUsername("receptionniste_test");
            receptionniste.setPassword(PasswordUtil.hashPassword("password123"));
            receptionniste.setFullName("Réceptionniste Test");
            receptionniste.setRole("réceptionniste");
            session.save(receptionniste);
            
            tx.commit();
        }
    }
    
    @BeforeEach
    void setUp() {
        employeeDAO = new EmployeeDAO();
        
        // Créer un employé de test
        testEmployee = new Employee();
        testEmployee.setUsername("test_user_" + System.currentTimeMillis());
        testEmployee.setPassword(PasswordUtil.hashPassword("testpass"));
        testEmployee.setFullName("Test User");
        testEmployee.setRole("concierge");
    }
    
    @AfterAll
    void tearDownDatabase() {
        HibernateTestUtil.shutdown();
    }
    
    @Test
    @DisplayName("Devrait récupérer un employé par nom d'utilisateur")
    void shouldGetEmployeeByUsername() {
        // When
        Employee employee = employeeDAO.getEmployeeByUsername("admin_test");
        
        // Then
        assertThat(employee).isNotNull();
        assertThat(employee.getUsername()).isEqualTo("admin_test");
        assertThat(employee.getFullName()).isEqualTo("Admin Test");
        assertThat(employee.getRole()).isEqualTo("admin");
    }
    
    @Test
    @DisplayName("Devrait retourner null pour un nom d'utilisateur inexistant")
    void shouldReturnNullForNonExistentUsername() {
        // When
        Employee employee = employeeDAO.getEmployeeByUsername("utilisateur_inexistant");
        
        // Then
        assertThat(employee).isNull();
    }
    
    @Test
    @DisplayName("Devrait valider les identifiants corrects")
    void shouldValidateCorrectCredentials() {
        // When
        Employee employee = employeeDAO.authenticate("admin_test", "password123");
        
        // Then
        assertThat(employee).isNotNull();
        assertThat(employee.getUsername()).isEqualTo("admin_test");
        assertThat(employee.getRole()).isEqualTo("admin");
    }
    
    @Test
    @DisplayName("Devrait rejeter les identifiants incorrects")
    void shouldRejectIncorrectCredentials() {
        // When
        Employee employee = employeeDAO.authenticate("admin_test", "mauvais_mot_de_passe");
        
        // Then
        assertThat(employee).isNull();
    }
    
    @Test
    @DisplayName("Devrait retourner null pour un utilisateur inexistant lors de la validation")
    void shouldReturnNullForNonExistentUserValidation() {
        // When
        Employee employee = employeeDAO.authenticate("utilisateur_inexistant", "password123");
        
        // Then
        assertThat(employee).isNull();
    }
    
    @Test
    @DisplayName("Devrait ajouter un nouvel employé")
    void shouldAddNewEmployee() {
        // When
        boolean result = employeeDAO.addEmployee(testEmployee);
        
        // Then
        assertThat(result).isTrue();
        
        // Vérifier que l'employé a été ajouté
        Employee savedEmployee = employeeDAO.getEmployeeByUsername(testEmployee.getUsername());
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFullName()).isEqualTo("Test User");
        assertThat(savedEmployee.getRole()).isEqualTo("concierge");
    }
    
    @Test
    @DisplayName("Devrait échouer l'ajout d'un employé avec un nom d'utilisateur existant")
    void shouldFailToAddEmployeeWithExistingUsername() {
        // Given
        Employee duplicateEmployee = new Employee();
        duplicateEmployee.setUsername("admin_test"); // Nom d'utilisateur déjà existant
        duplicateEmployee.setPassword(PasswordUtil.hashPassword("password"));
        duplicateEmployee.setFullName("Duplicate User");
        duplicateEmployee.setRole("staff");
        
        // When
        boolean result = employeeDAO.addEmployee(duplicateEmployee);
        
        // Then
        assertThat(result).isFalse();
    }
    
    @Test
    @DisplayName("Devrait récupérer tous les employés")
    void shouldGetAllEmployees() {
        // When
        List<Employee> employees = employeeDAO.getAllEmployees();
        
        // Then
        assertThat(employees).isNotNull();
        assertThat(employees).hasSizeGreaterThanOrEqualTo(2); // Au moins admin_test et receptionniste_test
        
        // Vérifier que les employés de test sont présents
        assertThat(employees)
            .extracting(Employee::getUsername)
            .contains("admin_test", "receptionniste_test");
    }
    
    @Test
    @DisplayName("Devrait récupérer un employé par ID")
    void shouldGetEmployeeById() {
        // Given
        Employee savedEmployee = employeeDAO.getEmployeeByUsername("admin_test");
        int employeeId = savedEmployee.getId();
        
        // When
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        
        // Then
        assertThat(employee).isNotNull();
        assertThat(employee.getId()).isEqualTo(employeeId);
        assertThat(employee.getUsername()).isEqualTo("admin_test");
    }
    
    @Test
    @DisplayName("Devrait retourner null pour un ID inexistant")
    void shouldReturnNullForNonExistentId() {
        // When
        Employee employee = employeeDAO.getEmployeeById(99999);
        
        // Then
        assertThat(employee).isNull();
    }
    
    @Test
    @DisplayName("Devrait mettre à jour un employé existant")
    void shouldUpdateExistingEmployee() {
        // Given
        employeeDAO.addEmployee(testEmployee);
        Employee savedEmployee = employeeDAO.getEmployeeByUsername(testEmployee.getUsername());
        
        // Modifier les informations
        savedEmployee.setFullName("Test User Updated");
        savedEmployee.setRole("chef de réception");
        
        // When
        boolean result = employeeDAO.updateEmployee(savedEmployee);
        
        // Then
        assertThat(result).isTrue();
        
        // Vérifier la mise à jour
        Employee updatedEmployee = employeeDAO.getEmployeeById(savedEmployee.getId());
        assertThat(updatedEmployee.getFullName()).isEqualTo("Test User Updated");
        assertThat(updatedEmployee.getRole()).isEqualTo("chef de réception");
    }
    
    @Test
    @DisplayName("Devrait supprimer un employé existant")
    void shouldDeleteExistingEmployee() {
        // Given
        employeeDAO.addEmployee(testEmployee);
        Employee savedEmployee = employeeDAO.getEmployeeByUsername(testEmployee.getUsername());
        int employeeId = savedEmployee.getId();
        
        // When
        boolean result = employeeDAO.deleteEmployee(employeeId);
        
        // Then
        assertThat(result).isTrue();
        
        // Vérifier la suppression
        Employee deletedEmployee = employeeDAO.getEmployeeById(employeeId);
        assertThat(deletedEmployee).isNull();
    }
    
    @Test
    @DisplayName("Devrait échouer la suppression d'un employé inexistant")
    void shouldFailToDeleteNonExistentEmployee() {
        // When
        boolean result = employeeDAO.deleteEmployee(99999);
        
        // Then
        assertThat(result).isFalse();
    }
    
    @Test
    @DisplayName("Devrait gérer les erreurs de base de données gracieusement")
    void shouldHandleDatabaseErrorsGracefully() {
        // Given - Employé avec des données invalides (nom d'utilisateur trop long)
        Employee invalidEmployee = new Employee();
        invalidEmployee.setUsername("nom_utilisateur_extremement_long_qui_depasse_la_limite_de_cinquante_caracteres");
        invalidEmployee.setPassword("password");
        invalidEmployee.setFullName("Test User");
        invalidEmployee.setRole("staff");
        
        // When
        boolean result = employeeDAO.addEmployee(invalidEmployee);
        
        // Then
        assertThat(result).isFalse();
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
            Employee employee = new Employee();
            employee.setUsername("test_" + role.replace(" ", "_") + "_" + System.currentTimeMillis());
            employee.setPassword(PasswordUtil.hashPassword("password123"));
            employee.setFullName("Test " + role);
            employee.setRole(role);
            
            boolean result = employeeDAO.addEmployee(employee);
            assertThat(result).isTrue();
            
            Employee savedEmployee = employeeDAO.getEmployeeByUsername(employee.getUsername());
            assertThat(savedEmployee.getRole()).isEqualTo(role);
        }
    }
}
