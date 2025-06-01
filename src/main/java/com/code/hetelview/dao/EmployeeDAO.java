package com.code.hetelview.dao;

import com.code.hetelview.model.Employee;
import com.code.hetelview.util.HibernateUtil;
import com.code.hetelview.util.PasswordUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Employee entities.
 */
public class EmployeeDAO {

    public EmployeeDAO() {
        // default constructor for Mockito
    }

    /**
     * Get an employee by username.
     * 
     * @param username The username to search for
     * @return The Employee object if found, null otherwise
     */
    public Employee getEmployeeByUsername(String username) {
        Transaction transaction = null;
        Employee employee = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();


            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);


            criteriaQuery.select(root).where(builder.equal(root.get("username"), username));


            employee = session.createQuery(criteriaQuery).uniqueResult();


            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error retrieving employee by username!");
            e.printStackTrace();
        }

        return employee;
    }

    /**
     * Authenticate an employee by username and password.
     * 
     * @param username The username
     * @param password The plain text password
     * @return The Employee object if authentication is successful, null otherwise
     */
    public Employee authenticate(String username, String password) {
        Transaction transaction = null;
        Employee employee = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            // First get the employee by username
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);

            criteriaQuery.select(root).where(builder.equal(root.get("username"), username));
            employee = session.createQuery(criteriaQuery).uniqueResult();

            // Verify password if employee found
            if (employee != null && !PasswordUtil.verifyPassword(password, employee.getPassword())) {
                employee = null; // Authentication failed
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error authenticating employee!");
            e.printStackTrace();
        }

        return employee;
    }

    /**
     * Get all employees.
     * 
     * @return A list of all employees
     */
    public List<Employee> getAllEmployees() {
        Transaction transaction = null;
        List<Employee> employees = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();


            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);
            criteriaQuery.select(root);


            employees = session.createQuery(criteriaQuery).getResultList();


            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error retrieving all employees!");
            e.printStackTrace();
        }

        return employees;
    }

    /**
     * Add a new employee.
     * 
     * @param employee The employee to add
     * @return true if the operation was successful, false otherwise
     */
    public boolean addEmployee(Employee employee) {
        Transaction transaction = null;
        boolean success = false;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start a transaction
            transaction = session.beginTransaction();

            // Hash the password before saving
            employee.setPassword(PasswordUtil.hashPassword(employee.getPassword()));

            // Save the employee
            session.save(employee);

            // Commit the transaction
            transaction.commit();
            success = true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error adding employee!");
            e.printStackTrace();
        }

        return success;
    }

    /**
     * Get an employee by ID.
     * 
     * @param id The employee ID to search for
     * @return The Employee object if found, null otherwise
     */
    public Employee getEmployeeById(int id) {
        Transaction transaction = null;
        Employee employee = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            employee = session.get(Employee.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error retrieving employee by ID!");
            e.printStackTrace();
        }

        return employee;
    }

    /**
     * Update an existing employee.
     * 
     * @param employee The employee to update
     * @return true if the operation was successful, false otherwise
     */
    public boolean updateEmployee(Employee employee) {
        Transaction transaction = null;
        boolean success = false;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Get the existing employee to preserve the current password if needed
            Employee existingEmployee = session.get(Employee.class, employee.getId());
            if (existingEmployee == null) {
                return false;
            }

            // Update fields
            existingEmployee.setUsername(employee.getUsername());
            existingEmployee.setFullName(employee.getFullName());
            existingEmployee.setRole(employee.getRole());

            // Only update password if a new password is provided and it's different
            if (employee.getPassword() != null && !employee.getPassword().trim().isEmpty()) {
                existingEmployee.setPassword(PasswordUtil.hashPassword(employee.getPassword()));
            }

            session.update(existingEmployee);
            transaction.commit();
            success = true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating employee!");
            e.printStackTrace();
        }

        return success;
    }

    /**
     * Delete an employee by ID.
     * 
     * @param id The ID of the employee to delete
     * @return true if the operation was successful, false otherwise
     */
    public boolean deleteEmployee(int id) {
        Transaction transaction = null;
        boolean success = false;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
                success = true;
            }
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting employee!");
            e.printStackTrace();
        }

        return success;
    }
}
