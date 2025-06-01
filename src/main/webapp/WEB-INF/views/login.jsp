<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hotel Management - Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Tailwind CSS from CDN -->
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
                    },
                    boxShadow: {
                        'elegant': '0 10px 25px -5px rgba(0, 0, 0, 0.05), 0 8px 10px -6px rgba(0, 0, 0, 0.03)',
                        'card': '0 20px 25px -5px rgba(0, 0, 0, 0.08), 0 10px 10px -5px rgba(0, 0, 0, 0.04)'
                    },
                    animation: {
                        'fade-in': 'fadeIn 0.5s ease-out forwards',
                        'slide-up': 'slideUp 0.5s ease-out forwards',
                        'pulse-slow': 'pulse 3s cubic-bezier(0.4, 0, 0.6, 1) infinite'
                    },
                    keyframes: {
                        fadeIn: {
                            '0%': { opacity: '0' },
                            '100%': { opacity: '1' }
                        },
                        slideUp: {
                            '0%': { transform: 'translateY(20px)', opacity: '0' },
                            '100%': { transform: 'translateY(0)', opacity: '1' }
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
            background-image: linear-gradient(to right bottom, #1E3A8A, #1E40AF);
            background-size: cover;
            background-attachment: fixed;
        }

        h1, h2, .font-serif {
            font-family: 'Playfair Display', serif;
        }

        .input-field {
            transition: border-color 0.2s ease, box-shadow 0.2s ease;
        }

        .input-field:focus {
            border-color: #B7791F;
            box-shadow: 0 0 0 3px rgba(183, 121, 31, 0.2);
        }

        .login-button {
            transition: transform 0.2s ease, box-shadow 0.2s ease, background-color 0.2s ease;
        }

        .login-button:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(30, 58, 138, 0.15);
        }

        .login-button:active {
            transform: translateY(0);
        }

        .card-decoration {
            background-image: url('https://images.pexels.com/photos/260922/pexels-photo-260922.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1');
            background-size: cover;
            background-position: center;
        }

        @media (max-width: 768px) {
            .card-decoration {
                height: 120px;
            }
        }
    </style>
</head>
<body class="min-h-screen flex items-center justify-center p-4 sm:p-6 md:p-8">
<div class="w-full max-w-4xl flex flex-col md:flex-row rounded-xl shadow-card overflow-hidden bg-white animate-fade-in">
    <!-- Decorative Left Panel (visible on md and up) -->
    <div class="card-decoration relative hidden md:block md:w-2/5 lg:w-1/2">
        <div class="absolute inset-0 bg-hotel-navy bg-opacity-30"></div>
        <div class="absolute bottom-0 left-0 right-0 p-8 text-white">
            <div class="font-serif text-3xl font-bold">Welcome</div>
            <p class="mt-2 opacity-90">Luxury at your service</p>
        </div>
    </div>

    <!-- Login Form -->
    <div class="w-full md:w-3/5 lg:w-1/2 p-6 sm:p-8 md:p-10 animate-slide-up" style="animation-delay: 0.1s">
        <!-- Logo and Title -->
        <div class="flex flex-col items-center text-center mb-8">
            <div class="w-16 h-16 flex items-center justify-center rounded-full bg-hotel-cream mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-hotel-gold" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M2 8h20M2 16h20M2 12h20M4 4v16M20 4v16" />
                </svg>
            </div>
            <h1 class="text-3xl sm:text-4xl font-bold text-hotel-charcoal">Hotel Management</h1>
            <div class="mt-2 text-base text-gray-600 flex items-center">
                <span class="h-px w-10 bg-hotel-gold opacity-50 mr-3"></span>
                <span>Employee Login</span>
                <span class="h-px w-10 bg-hotel-gold opacity-50 ml-3"></span>
            </div>
        </div>

        <!-- Error Message (if any) -->
        <% if (request.getAttribute("error") != null) { %>
        <div class="bg-red-50 border-l-4 border-red-500 text-red-700 p-4 rounded mb-6 animate-pulse-slow" role="alert">
            <div class="flex">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10"></circle>
                    <line x1="12" y1="8" x2="12" y2="12"></line>
                    <line x1="12" y1="16" x2="12.01" y2="16"></line>
                </svg>
                <span><%= request.getAttribute("error") %></span>
            </div>
        </div>
        <% } %>

        <!-- Login Form -->
        <form action="<%= request.getContextPath() %>/login" method="post" class="space-y-6">
            <div>
                <label for="username" class="block text-sm font-medium text-gray-700 mb-1">Username</label>
                <div class="relative">
                    <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                            <circle cx="12" cy="7" r="4"></circle>
                        </svg>
                    </div>
                    <input
                            type="text"
                            id="username"
                            name="username"
                            required
                            class="input-field pl-10 block w-full rounded-md border border-gray-300 bg-gray-50 py-3 text-gray-900 focus:outline-none"
                            placeholder="Enter your username"
                    >
                </div>
            </div>

            <div>
                <div class="flex items-center justify-between mb-1">
                    <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
                    <a href="#" class="text-xs text-hotel-navy hover:text-hotel-gold transition-colors">Forgot Password?</a>
                </div>
                <div class="relative">
                    <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                            <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                        </svg>
                    </div>
                    <input
                            type="password"
                            id="password"
                            name="password"
                            required
                            class="input-field pl-10 block w-full rounded-md border border-gray-300 bg-gray-50 py-3 text-gray-900 focus:outline-none"
                            placeholder="Enter your password"
                    >
                </div>
            </div>

            <div class="pt-2">
                <button
                        type="submit"
                        class="login-button w-full flex justify-center items-center py-3 px-4 border border-transparent rounded-md shadow-sm text-base font-medium text-white bg-gradient-to-r from-hotel-navy to-blue-800 hover:from-hotel-navy hover:to-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-hotel-navy"
                >
                    <span>Sign In</span>
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 ml-2" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M5 12h14M12 5l7 7-7 7"></path>
                    </svg>
                </button>
            </div>

            <div class="text-center text-sm text-gray-500 mt-8">
                <p>© 2025 Luxury Hotel Group. All rights reserved.</p>
            </div>
        </form>
    </div>
</div>
</body>
</html>