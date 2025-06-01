<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter un Employé - Système de Gestion Hôtelière</title>
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
            max-width: 500px;
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

        .form-group {
            margin-bottom: 20px;
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

        input, select {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        input:focus, select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 14px 28px;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;
            width: 100%;
            margin-bottom: 15px;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }

        .btn-secondary {
            background: #6c757d;
            text-decoration: none;
            display: inline-block;
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
        }

        .form-actions .btn {
            flex: 1;
            margin-bottom: 0;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <div class="form-header">
            <h1>Ajouter un Nouvel Employé</h1>
            <p>Créer un nouveau compte employé</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                ${error}
            </div>
        </c:if>

        <form action="employee-management" method="post">
            <input type="hidden" name="action" value="add">
            
            <div class="form-group">
                <label for="username">Nom d'utilisateur <span class="required">*</span></label>
                <input type="text" id="username" name="username" required 
                       value="${param.username}" placeholder="Entrer le nom d'utilisateur">
            </div>

            <div class="form-group">
                <label for="password">Mot de passe <span class="required">*</span></label>
                <input type="password" id="password" name="password" required 
                       placeholder="Entrer le mot de passe">
            </div>

            <div class="form-group">
                <label for="fullName">Nom complet <span class="required">*</span></label>
                <input type="text" id="fullName" name="fullName" required 
                       value="${param.fullName}" placeholder="Entrer le nom complet">
            </div>

            <div class="form-group">
                <label for="role">Rôle <span class="required">*</span></label>
                <select id="role" name="role" required>
                    <option value="">Sélectionner le rôle</option>
                    <option value="admin" ${param.role == 'admin' ? 'selected' : ''}>Admin</option>
                    <option value="staff" ${param.role == 'staff' ? 'selected' : ''}>Personnel</option>
                </select>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn">Ajouter l'Employé</button>
                <a href="employee-management" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</body>
</html>
