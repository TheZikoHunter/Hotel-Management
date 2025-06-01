package com.code.hetelview.dao;

import com.code.hetelview.model.Reservation;
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
 * Data Access Object for Reservation entities.
 */
public class ReservationDAO {

    public ReservationDAO() {
        // default constructor for Mockito
    }

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

    /**
     * Search reservations based on various criteria.
     * 
     * @param guestName Guest name to search for (partial match)
     * @param guestEmail Guest email to search for (partial match)
     * @param roomNumber Room number to filter by
     * @param status Status to filter by
     * @param checkInDateFrom Start date for check-in date range
     * @param checkInDateTo End date for check-in date range
     * @return List of reservations matching the criteria
     */
    public List<Reservation> searchReservations(String guestName, String guestEmail, 
                                               Integer roomNumber, String status,
                                               java.sql.Date checkInDateFrom, java.sql.Date checkInDateTo) {
        Transaction transaction = null;
        List<Reservation> reservations = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Reservation> criteriaQuery = builder.createQuery(Reservation.class);
            Root<Reservation> root = criteriaQuery.from(Reservation.class);

            List<Predicate> predicates = new ArrayList<>();

            // Add search criteria based on provided parameters
            if (guestName != null && !guestName.trim().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("guestName")), 
                              "%" + guestName.toLowerCase() + "%"));
            }

            if (guestEmail != null && !guestEmail.trim().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("guestEmail")), 
                              "%" + guestEmail.toLowerCase() + "%"));
            }

            if (roomNumber != null) {
                predicates.add(builder.equal(root.get("roomNumber"), roomNumber));
            }

            if (status != null && !status.trim().isEmpty()) {
                predicates.add(builder.equal(root.get("status"), status));
            }

            if (checkInDateFrom != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("checkInDate"), checkInDateFrom));
            }

            if (checkInDateTo != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("checkInDate"), checkInDateTo));
            }

            // Apply predicates
            if (!predicates.isEmpty()) {
                criteriaQuery.where(predicates.toArray(new Predicate[0]));
            }

            // Order by check-in date DESC
            criteriaQuery.select(root).orderBy(builder.desc(root.get("checkInDate")));

            reservations = session.createQuery(criteriaQuery).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error searching reservations!");
            e.printStackTrace();
        }

        return reservations;
    }

    /**
     * Get reservations by status.
     * 
     * @param status The status to filter by
     * @return List of reservations with the specified status
     */
    public List<Reservation> getReservationsByStatus(String status) {
        Transaction transaction = null;
        List<Reservation> reservations = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Reservation> criteriaQuery = builder.createQuery(Reservation.class);
            Root<Reservation> root = criteriaQuery.from(Reservation.class);

            criteriaQuery.select(root)
                        .where(builder.equal(root.get("status"), status))
                        .orderBy(builder.desc(root.get("checkInDate")));

            reservations = session.createQuery(criteriaQuery).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error retrieving reservations by status!");
            e.printStackTrace();
        }

        return reservations;
    }

}
