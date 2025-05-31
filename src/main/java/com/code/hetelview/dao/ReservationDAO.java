package com.code.hetelview.dao;

import com.code.hetelview.model.Employee;
import com.code.hetelview.model.Reservation;
import com.code.hetelview.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Reservation entities.
 */
public class ReservationDAO {

    /**
     * Get all reservations.
     * 
     * @return A list of all reservations
     */
    public List<Reservation> getAllReservations() {
        Transaction transaction = null;
        List<Reservation> reservations = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start a transaction
            transaction = session.beginTransaction();

            // Create criteria query
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Reservation> criteriaQuery = builder.createQuery(Reservation.class);
            Root<Reservation> root = criteriaQuery.from(Reservation.class);

            // Add order by check_in_date DESC
            criteriaQuery.select(root).orderBy(builder.desc(root.get("checkInDate")));

            // Execute query
            reservations = session.createQuery(criteriaQuery).getResultList();

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error retrieving all reservations!");
            e.printStackTrace();
        }

        return reservations;
    }

    /**
     * Get a reservation by ID.
     * 
     * @param id The ID of the reservation to retrieve
     * @return The Reservation object if found, null otherwise
     */
    public Reservation getReservationById(int id) {
        Transaction transaction = null;
        Reservation reservation = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();


            reservation = session.get(Reservation.class, id);


            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error retrieving reservation by ID!");
            e.printStackTrace();
        }

        return reservation;
    }

    /**
     * Add a new reservation.
     * 
     * @param reservation The reservation to add
     * @return true if the operation was successful, false otherwise
     */
    public boolean addReservation(Reservation reservation) {
        Transaction transaction = null;
        boolean success = false;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();


            session.save(reservation);


            transaction.commit();
            success = true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error adding reservation!");
            e.printStackTrace();
        }

        return success;
    }

    /**
     * Update an existing reservation.
     * 
     * @param reservation The reservation to update
     * @return true if the operation was successful, false otherwise
     */
    public boolean updateReservation(Reservation reservation) {
        Transaction transaction = null;
        boolean success = false;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();


            session.update(reservation);


            transaction.commit();
            success = true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating reservation!");
            e.printStackTrace();
        }

        return success;
    }

    /**
     * Delete a reservation.
     * 
     * @param id The ID of the reservation to delete
     * @return true if the operation was successful, false otherwise
     */
    public boolean deleteReservation(int id) {
        Transaction transaction = null;
        boolean success = false;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();


            Reservation reservation = session.get(Reservation.class, id);

            if (reservation != null) {

                session.delete(reservation);
                success = true;
            }


            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting reservation!");
            e.printStackTrace();
        }

        return success;
    }
}
