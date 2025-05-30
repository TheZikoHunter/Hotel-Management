import React, { useState, useEffect } from 'react';
import { User, Calendar, Plus, LogOut, Search, Filter, X } from 'lucide-react';

// LoginForm Component
const LoginForm = ({ onLogin }) => {
    const [loginData, setLoginData] = useState({
        username: '',
        password: ''
    });
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async () => {
        setIsLoading(true);

        try {
            // Simple auth - in production, validate against backend
            if (loginData.username === 'admin' && loginData.password === 'password') {
                const userData = {
                    userId: 'emp001',
                    username: loginData.username,
                    name: 'Hotel Employee',
                    role: 'receptionist'
                };

                // Store in cookie
                const expires = new Date();
                expires.setTime(expires.getTime() + (7 * 24 * 60 * 60 * 1000));
                document.cookie = `hotelUser=${JSON.stringify(userData)};expires=${expires.toUTCString()};path=/`;

                onLogin(userData);
            } else {
                alert('Invalid credentials. Use admin/password for demo.');
            }
        } catch (error) {
            console.error('Login error:', error);
            alert('Login failed. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleInputChange = (field, value) => {
        setLoginData(prev => ({
            ...prev,
            [field]: value
        }));
    };

    return (
        <div className="min-h-screen w-full bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
            <div className="bg-white rounded-lg shadow-xl p-8 w-full max-w-md">
                <div className="text-center mb-8">
                    <h1 className="text-3xl font-bold text-gray-800 mb-2">Hotel Dashboard</h1>
                    <p className="text-gray-600">Employee Login</p>
                </div>

                <div className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Username
                        </label>
                        <input
                            type="text"
                            required
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={loginData.username}
                            onChange={(e) => handleInputChange('username', e.target.value)}
                            placeholder="Enter username"
                            disabled={isLoading}
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Password
                        </label>
                        <input
                            type="password"
                            required
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={loginData.password}
                            onChange={(e) => handleInputChange('password', e.target.value)}
                            placeholder="Enter password"
                            disabled={isLoading}
                        />
                    </div>

                    <button
                        type="button"
                        onClick={handleSubmit}
                        disabled={isLoading || !loginData.username || !loginData.password}
                        className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        {isLoading ? 'Logging in...' : 'Login'}
                    </button>
                </div>

                <div className="mt-6 p-4 bg-blue-50 rounded-md">
                    <p className="text-sm text-blue-800">
                        <strong>Demo Credentials:</strong><br />
                        Username: admin<br />
                        Password: password
                    </p>
                </div>
            </div>
        </div>
    );
};

// AddReservationModal Component
const AddReservationModal = ({ onClose, onSave }) => {
    const [formData, setFormData] = useState({
        guestName: '',
        email: '',
        phone: '',
        checkIn: '',
        checkOut: '',
        roomType: 'standard',
        guests: 1,
        specialRequests: ''
    });

    const handleSubmit = () => {
        onSave(formData);
    };

    const handleChange = (field, value) => {
        setFormData(prev => ({
            ...prev,
            [field]: value
        }));
    };

    return (
        <div className="fixed inset-0 backdrop-blur-sm flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg shadow-xl w-full max-w-md max-h-screen overflow-y-auto">
                <div className="flex justify-between items-center p-6 border-b">
                    <h2 className="text-xl font-semibold text-gray-900">Add New Reservation</h2>
                    <button
                        onClick={onClose}
                        className="text-gray-400 hover:text-gray-600"
                    >
                        <X className="h-6 w-6" />
                    </button>
                </div>

                <div className="p-6 space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Guest Name *
                        </label>
                        <input
                            type="text"
                            required
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={formData.guestName}
                            onChange={(e) => handleChange('guestName', e.target.value)}
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Email *
                        </label>
                        <input
                            type="email"
                            required
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={formData.email}
                            onChange={(e) => handleChange('email', e.target.value)}
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Phone
                        </label>
                        <input
                            type="tel"
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={formData.phone}
                            onChange={(e) => handleChange('phone', e.target.value)}
                        />
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Check-in *
                            </label>
                            <input
                                type="date"
                                required
                                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={formData.checkIn}
                                onChange={(e) => handleChange('checkIn', e.target.value)}
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Check-out *
                            </label>
                            <input
                                type="date"
                                required
                                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={formData.checkOut}
                                onChange={(e) => handleChange('checkOut', e.target.value)}
                            />
                        </div>
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Room Type
                            </label>
                            <select
                                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={formData.roomType}
                                onChange={(e) => handleChange('roomType', e.target.value)}
                            >
                                <option value="standard">Standard</option>
                                <option value="deluxe">Deluxe</option>
                                <option value="suite">Suite</option>
                            </select>
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Guests
                            </label>
                            <input
                                type="number"
                                min="1"
                                max="10"
                                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={formData.guests}
                                onChange={(e) => handleChange('guests', parseInt(e.target.value))}
                            />
                        </div>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Special Requests
                        </label>
                        <textarea
                            rows={3}
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={formData.specialRequests}
                            onChange={(e) => handleChange('specialRequests', e.target.value)}
                            placeholder="Any special requests or notes..."
                        />
                    </div>

                    <div className="flex justify-end space-x-3 pt-4">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-4 py-2 text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300 transition duration-200"
                        >
                            Cancel
                        </button>
                        <button
                            type="button"
                            onClick={handleSubmit}
                            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition duration-200"
                        >
                            Save Reservation
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

// Dashboard Component
const Dashboard = ({ user, onLogout }) => {
    const [reservations, setReservations] = useState([]);
    const [showAddForm, setShowAddForm] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const [filterStatus, setFilterStatus] = useState('all');

    useEffect(() => {
        loadReservations();
    }, []);

    const loadReservations = () => {
        const sampleReservations = [
            {
                id: 'RES001',
                guestName: 'John Smith',
                email: 'john@email.com',
                phone: '+1-555-0123',
                checkIn: '2024-06-01',
                checkOut: '2024-06-05',
                roomType: 'deluxe',
                guests: 2,
                status: 'confirmed',
                specialRequests: 'Late check-in requested'
            },
            {
                id: 'RES002',
                guestName: 'Sarah Johnson',
                email: 'sarah@email.com',
                phone: '+1-555-0456',
                checkIn: '2024-06-03',
                checkOut: '2024-06-07',
                roomType: 'suite',
                guests: 3,
                status: 'pending',
                specialRequests: 'Ground floor room preferred'
            }
        ];
        setReservations(sampleReservations);
    };

    const handleAddReservation = (newReservation) => {
        const reservation = {
            id: 'RES' + String(Date.now()).slice(-6),
            ...newReservation,
            status: 'confirmed',
            createdBy: user.userId,
            createdAt: new Date().toISOString()
        };

        setReservations(prev => [...prev, reservation]);
        setShowAddForm(false);
    };

    const handleLogout = () => {
        // Clear cookie
        document.cookie = 'hotelUser=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        onLogout();
    };

    // Filter reservations
    const filteredReservations = reservations.filter(reservation => {
        const matchesSearch = reservation.guestName.toLowerCase().includes(searchTerm.toLowerCase()) ||
            reservation.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
            reservation.id.toLowerCase().includes(searchTerm.toLowerCase());

        const matchesFilter = filterStatus === 'all' || reservation.status === filterStatus;

        return matchesSearch && matchesFilter;
    });

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <header className="bg-white shadow-sm border-b">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between items-center h-16">
                        <div className="flex items-center">
                            <h1 className="text-2xl font-bold text-gray-900">Hotel Dashboard</h1>
                        </div>

                        <div className="flex items-center space-x-4">
                            <div className="flex items-center space-x-2">
                                <User className="h-5 w-5 text-gray-500" />
                                <span className="text-sm text-gray-700">{user.name}</span>
                            </div>

                            <button
                                onClick={handleLogout}
                                className="flex items-center space-x-1 text-red-600 hover:text-red-800"
                            >
                                <LogOut className="h-4 w-4" />
                                <span className="text-sm">Logout</span>
                            </button>
                        </div>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* Actions Bar */}
                <div className="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between space-y-4 sm:space-y-0">
                    <div className="flex items-center space-x-4">
                        <div className="relative">
                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                            <input
                                type="text"
                                placeholder="Search reservations..."
                                className="pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                            />
                        </div>

                        <div className="flex items-center space-x-2">
                            <Filter className="h-4 w-4 text-gray-500" />
                            <select
                                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={filterStatus}
                                onChange={(e) => setFilterStatus(e.target.value)}
                            >
                                <option value="all">All Status</option>
                                <option value="confirmed">Confirmed</option>
                                <option value="pending">Pending</option>
                                <option value="cancelled">Cancelled</option>
                            </select>
                        </div>
                    </div>

                    <button
                        onClick={() => setShowAddForm(true)}
                        className="flex items-center space-x-2 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition duration-200"
                    >
                        <Plus className="h-4 w-4" />
                        <span>Add Reservation</span>
                    </button>
                </div>

                {/* Reservations List */}
                <div className="bg-white shadow rounded-lg overflow-hidden">
                    <div className="px-6 py-4 border-b border-gray-200">
                        <h2 className="text-lg font-semibold text-gray-900">
                            Reservations ({filteredReservations.length})
                        </h2>
                    </div>

                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Reservation ID
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Guest Details
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Check-in/out
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Room & Guests
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Status
                                </th>
                            </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                            {filteredReservations.map((reservation) => (
                                <tr key={reservation.id} className="hover:bg-gray-50">
                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                        {reservation.id}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <div className="text-sm font-medium text-gray-900">{reservation.guestName}</div>
                                        <div className="text-sm text-gray-500">{reservation.email}</div>
                                        <div className="text-sm text-gray-500">{reservation.phone}</div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <div className="text-sm text-gray-900">
                                            <Calendar className="inline h-4 w-4 mr-1" />
                                            {reservation.checkIn} to {reservation.checkOut}
                                        </div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <div className="text-sm text-gray-900">
                                            {reservation.roomType.charAt(0).toUpperCase() + reservation.roomType.slice(1)}
                                        </div>
                                        <div className="text-sm text-gray-500">{reservation.guests} guest(s)</div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                          reservation.status === 'confirmed' ? 'bg-green-100 text-green-800' :
                              reservation.status === 'pending' ? 'bg-yellow-100 text-yellow-800' :
                                  'bg-red-100 text-red-800'
                      }`}>
                        {reservation.status}
                      </span>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>

                    {filteredReservations.length === 0 && (
                        <div className="text-center py-12">
                            <Calendar className="mx-auto h-12 w-12 text-gray-400" />
                            <h3 className="mt-2 text-sm font-medium text-gray-900">No reservations found</h3>
                            <p className="mt-1 text-sm text-gray-500">
                                {searchTerm || filterStatus !== 'all' ? 'Try adjusting your search or filter.' : 'Get started by adding a new reservation.'}
                            </p>
                        </div>
                    )}
                </div>
            </main>

            {/* Add Reservation Modal */}
            {showAddForm && (
                <AddReservationModal
                    onClose={() => setShowAddForm(false)}
                    onSave={handleAddReservation}
                />
            )}
        </div>
    );
};

// Main App Component
const HotelApp = () => {
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    // Check for existing auth on component mount
    useEffect(() => {
        checkExistingAuth();
    }, []);

    const checkExistingAuth = () => {
        try {
            const savedUser = getCookie('hotelUser');
            if (savedUser) {
                const userData = JSON.parse(savedUser);
                setUser(userData);
            }
        } catch (error) {
            console.error('Error parsing saved user data:', error);
            // Clear invalid cookie
            document.cookie = 'hotelUser=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        } finally {
            setIsLoading(false);
        }
    };

    const getCookie = (name) => {
        const nameEQ = name + "=";
        const ca = document.cookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    };

    const handleLogin = (userData) => {
        setUser(userData);
    };

    const handleLogout = () => {
        setUser(null);
    };

    if (isLoading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
                    <p className="mt-4 text-gray-600">Loading...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen w-full">
            {user ? (
                <Dashboard user={user} onLogout={handleLogout} />
            ) : (
                <LoginForm onLogin={handleLogin} />
            )}
        </div>
    );
};

export default HotelApp;