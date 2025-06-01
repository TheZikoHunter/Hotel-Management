<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Employés - Système de Gestion Hôtelière</title>
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
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }

        .header h1 {
            margin-bottom: 10px;
            font-size: 2.5rem;
        }

        .nav-links {
            background: #f8f9fa;
            padding: 15px 30px;
            border-bottom: 1px solid #dee2e6;
        }

        .nav-links a {
            color: #495057;
            text-decoration: none;
            margin-right: 20px;
            padding: 8px 16px;
            border-radius: 5px;
            transition: all 0.3s ease;
        }

        .nav-links a:hover {
            background: #e9ecef;
            color: #343a40;
        }

        .content {
            padding: 30px;
        }

        .actions {
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            text-decoration: none;
            display: inline-block;
            font-weight: 500;
            transition: all 0.3s ease;
            cursor: pointer;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }

        .btn-danger {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
        }

        .btn-warning {
            background: linear-gradient(135deg, #feca57 0%, #ff9ff3 100%);
            color: #333;
        }

        .table-container {
            overflow-x: auto;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #dee2e6;
        }

        th {
            background: #f8f9fa;
            font-weight: 600;
            color: #495057;
            position: sticky;
            top: 0;
        }

        tr:hover {
            background: #f8f9fa;
        }

        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
            font-weight: 500;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-danger {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 500;
        }

        .badge-admin {
            background: #dc3545;
            color: white;
        }

        .badge-staff {
            background: #007bff;
            color: white;
        }

        .action-buttons {
            display: flex;
            gap: 10px;
        }

        .action-buttons a, .action-buttons button {
            padding: 8px 16px;
            font-size: 0.9rem;
        }

        /* Delete Modal Styles */
        .modal-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 1000;
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .modal-overlay.show {
            display: flex;
            opacity: 1;
            align-items: center;
            justify-content: center;
        }

        .modal {
            background: white;
            border-radius: 12px;
            padding: 0;
            min-width: 400px;
            max-width: 500px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.3);
            transform: scale(0.7);
            transition: transform 0.3s ease;
            overflow: hidden;
        }

        .modal-overlay.show .modal {
            transform: scale(1);
        }

        .modal-header {
            background: linear-gradient(135deg, #dc3545, #c82333);
            color: white;
            padding: 20px 25px;
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .modal-icon {
            width: 40px;
            height: 40px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
        }

        .modal-title {
            font-size: 1.4rem;
            font-weight: 600;
            margin: 0;
        }

        .modal-body {
            padding: 25px;
            text-align: center;
        }

        .modal-message {
            color: #495057;
            font-size: 1.1rem;
            line-height: 1.5;
            margin-bottom: 25px;
        }

        .modal-actions {
            display: flex;
            gap: 15px;
            justify-content: center;
        }

        .modal-btn {
            padding: 12px 25px;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            min-width: 100px;
        }

        .modal-btn-cancel {
            background: #6c757d;
            color: white;
        }

        .modal-btn-cancel:hover {
            background: #5a6268;
            transform: translateY(-2px);
        }

        .modal-btn-confirm {
            background: #dc3545;
            color: white;
        }

        .modal-btn-confirm:hover {
            background: #c82333;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Gestion des Employés</h1>
            <p>Gérer le personnel et les administrateurs de l'hôtel</p>
        </div>

        <div class="nav-links">
            <a href="dashboard">← Retour au Tableau de Bord</a>
            <a href="employee-management">Tous les Employés</a>
        </div>

        <div class="content">
            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    ${success}
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    ${error}
                </div>
            </c:if>

            <div class="actions">
                <h2>Tous les Employés</h2>
                <a href="employee-management?action=add" class="btn">+ Ajouter un Nouvel Employé</a>
            </div>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nom d'utilisateur</th>
                            <th>Nom complet</th>
                            <th>Rôle</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="employee" items="${employees}">
                            <tr>
                                <td>${employee.id}</td>
                                <td>${employee.username}</td>
                                <td>${employee.fullName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${employee.role == 'admin'}">
                                            <span class="badge badge-admin">Admin</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-staff">Staff</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <a href="employee-management?action=edit&id=${employee.id}" class="btn btn-warning">Modifier</a>
                                        <c:if test="${sessionScope.employee.id != employee.id}">
                                            <button type="button" 
                                                    class="btn btn-danger"
                                                    onclick="showDeleteModal('${employee.id}', '${employee.fullName}')">Supprimer</button>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty employees}">
                            <tr>
                                <td colspan="5" style="text-align: center; padding: 30px; color: #6c757d;">
                                    Aucun employé trouvé.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div id="deleteModal" class="modal-overlay">
        <div class="modal">
            <div class="modal-header">
                <div class="modal-icon">⚠️</div>
                <h3 class="modal-title">Confirmer la Suppression</h3>
            </div>
            <div class="modal-body">
                <p class="modal-message">
                    Êtes-vous sûr de vouloir supprimer l'employé <strong id="employeeName"></strong>?
                    <br><br>
                    Cette action ne peut pas être annulée.
                </p>
                <div class="modal-actions">
                    <button type="button" class="modal-btn modal-btn-cancel" onclick="closeDeleteModal()">Annuler</button>
                    <button type="button" class="modal-btn modal-btn-confirm" onclick="confirmDelete()">Supprimer</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Hidden form for delete action -->
    <form id="deleteForm" method="post" action="employee-management" style="display: none;">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="id" id="deleteEmployeeId">
    </form>

    <script>
        let employeeToDelete = null;

        function showDeleteModal(employeeId, employeeName) {
            employeeToDelete = employeeId;
            document.getElementById('employeeName').textContent = employeeName;
            const modal = document.getElementById('deleteModal');
            modal.classList.add('show');
            
            // Focus trap for accessibility
            const confirmBtn = modal.querySelector('.modal-btn-confirm');
            confirmBtn.focus();
        }

        function closeDeleteModal() {
            const modal = document.getElementById('deleteModal');
            modal.classList.remove('show');
            employeeToDelete = null;
        }

        function confirmDelete() {
            if (employeeToDelete) {
                document.getElementById('deleteEmployeeId').value = employeeToDelete;
                document.getElementById('deleteForm').submit();
            }
        }

        // Close modal when clicking outside
        document.getElementById('deleteModal').addEventListener('click', function(e) {
            if (e.target === this) {
                closeDeleteModal();
            }
        });

        // Close modal on escape key
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeDeleteModal();
            }
        });
    </script>
</body>
</html>
