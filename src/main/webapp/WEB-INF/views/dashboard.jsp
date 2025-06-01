<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.code.hetelview.model.Reservation" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hotel Management - Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        hotel: {
                            navy: '#1E3A8A',
                            gold: '#B7791F',
                            cream: '#FEF3C7',
                            charcoal: '#1F2937'
                        }
                    }
                }
            }
        }
    </script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Playfair+Display:wght@500;700&family=Poppins:wght@300;400;500&display=swap');

        body {
            font-family: 'Poppins', sans-serif;
        }

        h1, h2, .font-serif {
            font-family: 'Playfair Display', serif;
        }

        .table-hover tr:hover {
            background-color: rgba(243, 244, 246, 0.5);
            transition: all 0.2s ease;
        }

        .status-badge {
            transition: all 0.2s ease;
        }

        .status-badge:hover {
            transform: translateY(-1px);
        }

        .action-button {
            transition: all 0.2s ease;
        }

        .action-button:hover {
            transform: translateY(-1px);
        }

        /* Custom Delete Confirmation Modal */
        .modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 1000;
            opacity: 0;
            visibility: hidden;
            transition: all 0.3s ease;
        }

        .modal-overlay.active {
            opacity: 1;
            visibility: visible;
        }

        .modal-content {
            background: white;
            border-radius: 16px;
            padding: 32px;
            max-width: 440px;
            width: 90%;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
            transform: scale(0.9) translateY(-20px);
            transition: transform 0.3s ease;
        }

        .modal-overlay.active .modal-content {
            transform: scale(1) translateY(0);
        }

        .modal-icon {
            width: 64px;
            height: 64px;
            background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 24px;
        }

        .modal-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #1f2937;
            text-align: center;
            margin-bottom: 12px;
        }

        .modal-message {
            color: #6b7280;
            text-align: center;
            margin-bottom: 32px;
            line-height: 1.6;
        }

        .modal-actions {
            display: flex;
            gap: 12px;
        }

        .modal-btn {
            flex: 1;
            padding: 12px 24px;
            border-radius: 8px;
            font-weight: 500;
            font-size: 0.95rem;
            cursor: pointer;
            transition: all 0.2s ease;
            border: none;
            outline: none;
        }

        .modal-btn-cancel {
            background: #f3f4f6;
            color: #374151;
        }

        .modal-btn-cancel:hover {
            background: #e5e7eb;
        }

        .modal-btn-delete {
            background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
            color: white;
        }

        .modal-btn-delete:hover {
            background: linear-gradient(135deg, #dc2626 0%, #b91c1c 100%);
            transform: translateY(-1px);
        }
    </style>
</head>
<body class="bg-gray-50 min-h-screen">
<!-- Navigation Bar -->
<nav class="bg-gradient-to-r from-hotel-navy to-blue-800 text-white shadow-lg">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-20">
            <div class="flex items-center space-x-4">
                <div class="w-10 h-10 bg-white/10 rounded-full flex items-center justify-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                    </svg>
                </div>
                <span class="text-2xl font-serif">Hotel Management System</span>
            </div>
            <div class="flex items-center space-x-6">
                <div class="flex items-center space-x-2">
                    <div class="w-8 h-8 bg-white/10 rounded-full flex items-center justify-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                        </svg>
                    </div>
                    <span class="text-sm font-medium">Welcome, <%= session.getAttribute("employeeName") %></span>
                </div>
                <a href="logout" class="bg-red-500 hover:bg-red-600 text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors duration-150 ease-in-out flex items-center space-x-2">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                    </svg>
                    <span>Logout</span>
                </a>
            </div>
        </div>
    </div>
</nav>

<!-- Main Content -->
<div class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
    <!-- Page Header -->
    <div class="mb-8">
        <div class="flex justify-between items-center">
            <div>
                <h1 class="text-3xl font-serif text-hotel-charcoal">Reservations</h1>
                <p class="mt-1 text-sm text-gray-500">Manage hotel reservations and guest bookings</p>
            </div>
            <div class="flex space-x-4">
                <%-- Show employee management link only for admin users --%>
                <% 
                    com.code.hetelview.model.Employee currentEmployee = (com.code.hetelview.model.Employee) session.getAttribute("employee");
                    if (currentEmployee != null && "admin".equalsIgnoreCase(currentEmployee.getRole())) {
                %>
                <a href="employee-management" class="bg-purple-600 hover:bg-purple-700 text-white text-sm font-medium px-6 py-3 rounded-lg shadow-sm transition-all duration-150 ease-in-out flex items-center space-x-2">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z" />
                    </svg>
                    <span>Manage Employees</span>
                </a>
                <% } %>
                <a href="add-reservation" class="bg-hotel-navy hover:bg-blue-800 text-white text-sm font-medium px-6 py-3 rounded-lg shadow-sm transition-all duration-150 ease-in-out flex items-center space-x-2">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                    </svg>
                    <span>Add New Reservation</span>
                </a>
            </div>
        </div>
    </div>

    <!-- Success/Error Messages -->
    <% if (request.getParameter("success") != null) { %>
    <div class="mb-6 bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg" role="alert">
        <div class="flex">
            <div class="flex-shrink-0">
                <svg class="h-5 w-5 text-green-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                    <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                </svg>
            </div>
            <div class="ml-3">
                <p class="text-sm font-medium"><%= request.getParameter("success") %></p>
            </div>
        </div>
    </div>
    <% } %>
    
    <% if (request.getParameter("error") != null) { %>
    <div class="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg" role="alert">
        <div class="flex">
            <div class="flex-shrink-0">
                <svg class="h-5 w-5 text-red-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                    <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L10 8.586l1.293-1.293a1 1 0 001.414-1.414L10 5.586 8.707 7.293z" clipRule="evenodd" />
                </svg>
            </div>
            <div class="ml-3">
                <p class="text-sm font-medium"><%= request.getParameter("error") %></p>
            </div>
        </div>
    </div>
    <% } %>

    <!-- Reservations Table -->
    <div class="bg-white rounded-xl shadow-sm overflow-hidden border border-gray-100">
        <table class="min-w-full divide-y divide-gray-200 table-hover">
            <thead>
            <tr class="bg-gray-50">
                <th scope="col" class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Guest Name</th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Room</th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Check-in</th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Check-out</th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
            </thead>
            <tbody class="divide-y divide-gray-200 bg-white">
            <%
                List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                if (reservations != null && !reservations.isEmpty()) {
                    for (Reservation reservation : reservations) {
            %>
            <tr class="hover:bg-gray-50 transition-colors duration-150 ease-in-out">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">#<%= reservation.getId() %></td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <div class="text-sm font-medium text-gray-900"><%= reservation.getGuestName() %></div>
                    <div class="text-sm text-gray-500"><%= reservation.getGuestEmail() %></div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <div class="text-sm text-gray-900 font-medium">Room <%= reservation.getRoomNumber() %></div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= dateFormat.format(reservation.getCheckInDate()) %></td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= dateFormat.format(reservation.getCheckOutDate()) %></td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <% if (reservation.getStatus().equalsIgnoreCase("confirmed")) { %>
                    <span class="status-badge px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                    Confirmed
                                </span>
                    <% } else if (reservation.getStatus().equalsIgnoreCase("checked-in")) { %>
                    <span class="status-badge px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                                    Checked-in
                                </span>
                    <% } else if (reservation.getStatus().equalsIgnoreCase("checked-out")) { %>
                    <span class="status-badge px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">
                                    Checked-out
                                </span>
                    <% } else if (reservation.getStatus().equalsIgnoreCase("cancelled")) { %>
                    <span class="status-badge px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                                    Cancelled
                                </span>
                    <% } else { %>
                    <span class="status-badge px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800">
                                    <%= reservation.getStatus() %>
                                </span>
                    <% } %>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <div class="flex space-x-3">
                        <a href="edit-reservation?id=<%= reservation.getId() %>" class="action-button text-blue-600 hover:text-blue-900">Edit</a>
                        <button type="button" class="action-button text-red-600 hover:text-red-900 border-none bg-transparent cursor-pointer delete-btn"
                                data-id="<%= reservation.getId() %>" data-name="<%= reservation.getGuestName() %>">Delete</button>
                    </div>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="7" class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 text-center">No reservations found</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div id="deleteModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-icon">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1-1H8a1 1 0 00-1 1v3M4 7h16" />
            </svg>
        </div>
        <h3 class="modal-title">Delete Reservation</h3>
        <p class="modal-message">
            Are you sure you want to delete the reservation for <strong id="guestName"></strong>? 
            This action cannot be undone.
        </p>
        <div class="modal-actions">
            <button type="button" class="modal-btn modal-btn-cancel" onclick="closeDeleteModal()">Cancel</button>
            <button type="button" class="modal-btn modal-btn-delete" onclick="confirmDelete()">Delete Reservation</button>
        </div>
    </div>
</div>

<script>
    let reservationToDelete = null;

    // Show delete confirmation modal
    function showDeleteModal(reservationId, guestName) {
        reservationToDelete = reservationId;
        document.getElementById('guestName').textContent = guestName;
        document.getElementById('deleteModal').classList.add('active');
        document.body.style.overflow = 'hidden'; // Prevent background scrolling
    }

    // Close delete confirmation modal
    function closeDeleteModal() {
        document.getElementById('deleteModal').classList.remove('active');
        document.body.style.overflow = 'auto'; // Restore scrolling
        reservationToDelete = null;
    }

    // Confirm delete action
    function confirmDelete() {
        if (reservationToDelete) {
            // Create a form and submit it to delete the reservation
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'reservation-management';
            
            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'delete';
            
            const idInput = document.createElement('input');
            idInput.type = 'hidden';
            idInput.name = 'id';
            idInput.value = reservationToDelete;
            
            form.appendChild(actionInput);
            form.appendChild(idInput);
            document.body.appendChild(form);
            form.submit();
        }
    }

    // Close modal when clicking outside
    document.getElementById('deleteModal').addEventListener('click', function(e) {
        if (e.target === this) {
            closeDeleteModal();
        }
    });

    // Close modal with Escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && document.getElementById('deleteModal').classList.contains('active')) {
            closeDeleteModal();
        }
    });

    // Attach event listeners to delete buttons
    document.addEventListener('DOMContentLoaded', function() {
        const deleteButtons = document.querySelectorAll('.delete-btn');
        deleteButtons.forEach(button => {
            button.addEventListener('click', function(e) {
                e.preventDefault();
                const reservationId = this.getAttribute('data-id');
                const guestName = this.getAttribute('data-name');
                showDeleteModal(reservationId, guestName);
            });
        });
    });
</script>

</body>
</html>