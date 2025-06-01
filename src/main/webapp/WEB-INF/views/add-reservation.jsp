<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestion Hôtelière - Ajouter une Réservation</title>
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

        .form-input {
            transition: all 0.2s ease;
        }

        .form-input:focus {
            border-color: #1E3A8A;
            box-shadow: 0 0 0 3px rgba(30, 58, 138, 0.1);
        }

        .submit-button {
            transition: all 0.2s ease;
        }

        .submit-button:hover {
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
                <span class="text-2xl font-serif">Système de Gestion Hôtelière</span>
            </div>
            <div class="flex items-center space-x-6">
                <div class="flex items-center space-x-2">
                    <div class="w-8 h-8 bg-white/10 rounded-full flex items-center justify-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                        </svg>
                    </div>
                    <span class="text-sm font-medium">Bienvenue, <%= session.getAttribute("employeeName") %></span>
                </div>
                <a href="logout" class="bg-red-500 hover:bg-red-600 text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors duration-150 ease-in-out flex items-center space-x-2">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                    </svg>
                    <span>Déconnexion</span>
                </a>
            </div>
        </div>
    </div>
</nav>

<!-- Main Content -->
<div class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
    <!-- Page Header -->
    <div class="mb-8">
        <div class="flex items-center space-x-4">
            <a href="dashboard" class="text-hotel-navy hover:text-blue-800 transition-colors duration-150">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                </svg>
            </a>
            <div>
                <h1 class="text-3xl font-serif text-hotel-charcoal">Ajouter une Nouvelle Réservation</h1>
                <p class="mt-1 text-sm text-gray-500">Créer une nouvelle réservation pour un client</p>
            </div>
        </div>
    </div>

    <!-- Reservation Form -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <% if (request.getAttribute("error") != null) { %>
        <div class="bg-red-50 border-l-4 border-red-500 p-4" role="alert">
            <div class="flex">
                <div class="flex-shrink-0">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
                        <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                    </svg>
                </div>
                <div class="ml-3">
                    <p class="text-sm text-red-700"><%= request.getAttribute("error") %></p>
                </div>
            </div>
        </div>
        <% } %>

        <form action="add-reservation" method="post" class="p-8">
            <!-- Guest Information -->
            <div class="mb-8">
                <h2 class="text-xl font-serif text-hotel-charcoal mb-6">Informations du Client</h2>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div>
                        <label for="guestName" class="block text-sm font-medium text-gray-700 mb-2">Nom du Client</label>
                        <input type="text" id="guestName" name="guestName" required
                               class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none">
                    </div>

                    <div>
                        <label for="guestEmail" class="block text-sm font-medium text-gray-700 mb-2">Email</label>
                        <input type="email" id="guestEmail" name="guestEmail" required
                               class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none">
                    </div>

                    <div>
                        <label for="guestPhone" class="block text-sm font-medium text-gray-700 mb-2">Téléphone</label>
                        <input type="tel" id="guestPhone" name="guestPhone" required
                               class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none">
                    </div>
                </div>
            </div>

            <!-- Reservation Details -->
            <div class="mb-8">
                <h2 class="text-xl font-serif text-hotel-charcoal mb-6">Détails de la Réservation</h2>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div>
                        <label for="roomNumber" class="block text-sm font-medium text-gray-700 mb-2">Numéro de Chambre</label>
                        <input type="number" id="roomNumber" name="roomNumber" min="1" required
                               class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none">
                    </div>

                    <div>
                        <label for="checkInDate" class="block text-sm font-medium text-gray-700 mb-2">Date d'Arrivée</label>
                        <input type="date" id="checkInDate" name="checkInDate" required
                               class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none">
                    </div>

                    <div>
                        <label for="checkOutDate" class="block text-sm font-medium text-gray-700 mb-2">Date de Départ</label>
                        <input type="date" id="checkOutDate" name="checkOutDate" required
                               class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none">
                    </div>

                    <div>
                        <label for="status" class="block text-sm font-medium text-gray-700 mb-2">Statut</label>
                        <select id="status" name="status" required
                                class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none">
                            <option value="Confirmed">Confirmé</option>
                            <option value="Pending">En attente</option>
                            <option value="Cancelled">Annulé</option>
                        </select>
                    </div>

                    <div class="md:col-span-2">
                        <label for="notes" class="block text-sm font-medium text-gray-700 mb-2">Notes</label>
                        <textarea id="notes" name="notes" rows="3"
                                  class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none"></textarea>
                    </div>
                </div>
            </div>

            <!-- Form Actions -->
            <div class="flex justify-end space-x-4">
                <a href="dashboard"
                   class="px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 font-medium text-sm transition-colors duration-150">
                    Annuler
                </a>
                <button type="submit"
                        class="submit-button px-6 py-3 bg-hotel-navy text-white rounded-lg hover:bg-blue-800 font-medium text-sm shadow-sm">
                    Enregistrer la Réservation
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>