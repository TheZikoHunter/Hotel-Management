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
            <a href="add-reservation" class="bg-hotel-navy hover:bg-blue-800 text-white text-sm font-medium px-6 py-3 rounded-lg shadow-sm transition-all duration-150 ease-in-out flex items-center space-x-2">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                </svg>
                <span>Add New Reservation</span>
            </a>
        </div>
    </div>

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
                        <a href="delete-reservation?id=<%= reservation.getId() %>"
                           class="action-button text-red-600 hover:text-red-900"
                           onclick="return confirm('Are you sure you want to delete this reservation?')">Delete</a>
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
</body>
</html>