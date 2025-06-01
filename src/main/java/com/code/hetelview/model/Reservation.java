package com.code.hetelview.model;

import java.sql.Date;
import javax.persistence.*;

/**
 * Represents a hotel reservation made by a guest.
 */
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "guest_name", nullable = false, length = 100)
    private String guestName;

    @Column(name = "guest_email", nullable = false, length = 100)
    private String guestEmail;

    @Column(name = "guest_phone", nullable = false, length = 20)
    private String guestPhone;

    @Column(name = "room_number", nullable = false)
    private int roomNumber;

    @Column(name = "check_in_date", nullable = false)
    private Date checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private Date checkOutDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Employee createdBy;

    @Column(name = "notes")
    private String notes;

    // Default constructor
    public Reservation() {
    }

    // Parameterized constructor
    public Reservation(int id, String guestName, String guestEmail, String guestPhone,
                       int roomNumber, Date checkInDate, Date checkOutDate,
                       String status, Employee createdBy, String notes) {
        this.id = id;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.guestPhone = guestPhone;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.createdBy = createdBy;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employee createdBy) {
        this.createdBy = createdBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", guestName='" + guestName + '\'' +
                ", guestEmail='" + guestEmail + '\'' +
                ", guestPhone='" + guestPhone + '\'' +
                ", roomNumber=" + roomNumber +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", status='" + status + '\'' +
                ", createdBy=" + createdBy +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reservation that = (Reservation) obj;
        return id == that.id &&
               roomNumber == that.roomNumber &&
               java.util.Objects.equals(guestName, that.guestName) &&
               java.util.Objects.equals(guestEmail, that.guestEmail) &&
               java.util.Objects.equals(guestPhone, that.guestPhone) &&
               java.util.Objects.equals(checkInDate, that.checkInDate) &&
               java.util.Objects.equals(checkOutDate, that.checkOutDate) &&
               java.util.Objects.equals(status, that.status) &&
               java.util.Objects.equals(createdBy, that.createdBy) &&
               java.util.Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, guestName, guestEmail, guestPhone, roomNumber, 
                                    checkInDate, checkOutDate, status, createdBy, notes);
    }
}
