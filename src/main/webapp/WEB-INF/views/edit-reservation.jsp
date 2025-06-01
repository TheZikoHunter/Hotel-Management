<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Reservation - Hotel Management System</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .form-container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 800px;
        }

        .form-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .form-header h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 2rem;
        }

        .form-header p {
            color: #666;
            font-size: 1.1rem;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group.full-width {
            grid-column: 1 / -1;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
        }

        .required {
            color: #dc3545;
        }

        input, select, textarea {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        input:focus, select:focus, textarea:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        textarea {
            resize: vertical;
            min-height: 100px;
        }

        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 14px 28px;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            margin-bottom: 10px;
            text-align: center;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
            color: white;
            text-decoration: none;
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
            text-align: center;
        }

        .btn-secondary:hover {
            background: #545b62;
            color: white;
            text-decoration: none;
        }

        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
            font-weight: 500;
        }

        .alert-danger {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .form-actions {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }

        .form-actions .btn {
            flex: 1;
            margin-bottom: 0;
        }

        .section-title {
            color: #333;
            font-size: 1.3rem;
            font-weight: 600;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #e9ecef;
        }

        @media (max-width: 768px) {
            .form-grid {
                grid-template-columns: 1fr;
            }
            
            .form-container {
                padding: 20px;
            }
            
            .form-actions {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <div class="form-container">
        <div class="form-header">
            <h1>Edit Reservation</h1>
            <p>Update reservation information</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                ${error}
            </div>
        </c:if>

        <form action="edit-reservation" method="post">
            <input type="hidden" name="id" value="${reservation.id}">
            
            <!-- Guest Information Section -->
            <div class="section-title">Guest Information</div>
            <div class="form-grid">
                <div class="form-group">
                    <label for="guestName">Guest Name <span class="required">*</span></label>
                    <input type="text" id="guestName" name="guestName" required 
                           value="${reservation.guestName}" placeholder="Enter guest name">
                </div>

                <div class="form-group">
                    <label for="guestEmail">Email <span class="required">*</span></label>
                    <input type="email" id="guestEmail" name="guestEmail" required 
                           value="${reservation.guestEmail}" placeholder="Enter email address">
                </div>

                <div class="form-group">
                    <label for="guestPhone">Phone <span class="required">*</span></label>
                    <input type="tel" id="guestPhone" name="guestPhone" required 
                           value="${reservation.guestPhone}" placeholder="Enter phone number">
                </div>

                <div class="form-group">
                    <label for="roomNumber">Room Number <span class="required">*</span></label>
                    <input type="number" id="roomNumber" name="roomNumber" min="1" required 
                           value="${reservation.roomNumber}" placeholder="Enter room number">
                </div>
            </div>

            <!-- Reservation Details Section -->
            <div class="section-title">Reservation Details</div>
            <div class="form-grid">
                <div class="form-group">
                    <label for="checkInDate">Check-in Date <span class="required">*</span></label>
                    <input type="date" id="checkInDate" name="checkInDate" required 
                           value="${reservation.checkInDate}">
                </div>

                <div class="form-group">
                    <label for="checkOutDate">Check-out Date <span class="required">*</span></label>
                    <input type="date" id="checkOutDate" name="checkOutDate" required 
                           value="${reservation.checkOutDate}">
                </div>

                <div class="form-group">
                    <label for="status">Status <span class="required">*</span></label>
                    <select id="status" name="status" required>
                        <option value="">Select status</option>
                        <option value="Confirmed" ${reservation.status == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                        <option value="Pending" ${reservation.status == 'Pending' ? 'selected' : ''}>Pending</option>
                        <option value="Checked-in" ${reservation.status == 'Checked-in' ? 'selected' : ''}>Checked-in</option>
                        <option value="Checked-out" ${reservation.status == 'Checked-out' ? 'selected' : ''}>Checked-out</option>
                        <option value="Cancelled" ${reservation.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                    </select>
                </div>
            </div>

            <div class="form-group full-width">
                <label for="notes">Notes</label>
                <textarea id="notes" name="notes" placeholder="Enter any additional notes...">${reservation.notes}</textarea>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn">Update Reservation</button>
                <a href="dashboard" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>

    <script>
        // Set minimum date for check-in to today
        document.getElementById('checkInDate').min = new Date().toISOString().split('T')[0];
        
        // Ensure check-out date is after check-in date
        document.getElementById('checkInDate').addEventListener('change', function() {
            const checkInDate = new Date(this.value);
            const nextDay = new Date(checkInDate);
            nextDay.setDate(checkInDate.getDate() + 1);
            document.getElementById('checkOutDate').min = nextDay.toISOString().split('T')[0];
        });
    </script>
</body>
</html>
