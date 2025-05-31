package com.code.hetelview.dao;

import com.code.hetelview.model.Employee;
import com.code.hetelview.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Employee entities.
 */
public class EmployeeDAO {

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
     * @param password The password
     * @return The Employee object if authentication is successful, null otherwise
     */
    public Employee authenticate(String username, String password) {
        Transaction transaction = null;
        Employee employee = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();


            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);


            Predicate[] predicates = new Predicate[2];
            predicates[0] = builder.equal(root.get("username"), username);
            predicates[1] = builder.equal(root.get("password"), password); // In a real application, use password hashing

            criteriaQuery.select(root).where(predicates);


            employee = session.createQuery(criteriaQuery).uniqueResult();


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
}
