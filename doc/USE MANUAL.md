# Manuel d'utilisation - Système de Gestion Hôtelière

## 🔐 Connexion à l'application

Après le lancement de l'application, vous trouverez une page de connexion. Voici les comptes de test disponibles :

### Comptes administrateurs
- **Admin principal**
  - Nom d'utilisateur : `admin`
  - Mot de passe : `admin123`

### Comptes de gestion
- **Chef de réception**
  - Nom d'utilisateur : `chef_reception`
  - Mot de passe : `password123`

- **Gouvernante**
  - Nom d'utilisateur : `gouvernante1`
  - Mot de passe : `password123`

### Comptes employés
- **Réceptionniste**
  - Nom d'utilisateur : `receptionniste1` ou `receptionniste2`
  - Mot de passe : `password123`

---

## 🏠 Navigation principale

### Barre de navigation
Une fois connecté, vous verrez une barre de navigation en haut avec :
- **Logo et titre** : "Système de Gestion Hôtelière"
- **Nom d'utilisateur** : Affiche "Bienvenue, [Votre nom]"
- **Bouton de déconnexion** : Pour quitter l'application

### Boutons d'action principaux
Selon votre rôle, vous verrez différents boutons :

#### Pour Admin, Chef de réception et Gouvernante
- **"Gérer les Employés"** (bouton violet) : Accès à la gestion du personnel

#### Pour Réceptionniste
- **"Ajouter une Réservation"** (bouton bleu) : Créer une nouvelle réservation

---

## 📋 Gestion des Réservations

### Tableau de bord des réservations
La page principale affiche toutes les réservations avec :

#### Informations affichées
- **Client** : Nom, email et téléphone
- **Chambre** : Numéro de chambre
- **Dates** : Arrivée et départ
- **Statut** : Confirmé, Arrivé, Parti, Annulé
- **Actions** : Modifier, Supprimer

#### Recherche et filtrage
Utilisez le formulaire de recherche pour filtrer par :
- **Nom du client** : Recherche partielle
- **Email du client** : Recherche par adresse email
- **Numéro de chambre** : Recherche par numéro exact
- **Statut** : Filtrer par statut de réservation
- **Dates d'arrivée** : Période "de" et "à"

#### Actions sur les réservations
- **Bouton "Rechercher"** : Applique les filtres
- **Bouton "Effacer"** : Remet à zéro tous les filtres
- **Bouton "Modifier"** : Éditer une réservation existante
- **Bouton "Supprimer"** : Supprimer une réservation (avec confirmation)

### Ajouter une réservation
1. Cliquez sur **"Ajouter une Réservation"**
2. Remplissez les informations :
   - Nom du client
   - Email du client
   - Téléphone du client
   - Numéro de chambre
   - Date d'arrivée
   - Date de départ
   - Notes (optionnel)
3. Cliquez sur **"Créer la réservation"**

### Modifier une réservation
1. Cliquez sur le bouton **"Modifier"** dans le tableau
2. Modifiez les informations nécessaires
3. Cliquez sur **"Mettre à jour la réservation"**

---

## 👥 Gestion des Employés

*Accessible uniquement aux : Admin, Chef de réception, Gouvernante*

### Tableau des employés
Affiche la liste de tous les employés avec :
- **Nom d'utilisateur**
- **Nom complet**
- **Rôle**
- **Actions** : Modifier, Supprimer

### Recherche d'employés
Filtrez la liste par :
- **Nom d'utilisateur** : Recherche partielle
- **Nom complet** : Recherche partielle
- **Rôle** : Sélection dans une liste déroulante

### Ajouter un employé
1. Cliquez sur **"Ajouter un Employé"**
2. Remplissez les informations :
   - Nom d'utilisateur (unique)
   - Mot de passe
   - Nom complet
   - Rôle (selon vos permissions)
3. Cliquez sur **"Ajouter l'employé"**

### Modifier un employé
1. Cliquez sur le bouton **"Modifier"** dans le tableau
2. Modifiez les informations (laissez le mot de passe vide si vous ne souhaitez pas le changer)
3. Cliquez sur **"Mettre à jour l'employé"**

---

## 🔐 Permissions et Rôles

### Hiérarchie des permissions

#### Admin
- **Accès complet** à toutes les fonctionnalités
- Peut gérer tous les types d'employés
- Voir et gérer toutes les réservations

#### Chef de réception
- Gérer les réservations
- Gérer les employés de réception et services généraux :
  - Réceptionniste
  - Standardiste
  - Voiturier
  - Concierge
  - Agent de sécurité
  - Bagagiste
  - Guide
  - Caissier

#### Gouvernante
- Voir les réservations
- Gérer les employés de l'étage et du ménage :
  - Femme de chambre
  - Valet de chambre
  - Serveur d'étage
  - Lingère

#### Réceptionniste
- Gérer les réservations (voir, ajouter, modifier, supprimer)
- Pas d'accès à la gestion des employés

---

## 💡 Conseils d'utilisation

### Messages de confirmation
- Les actions de suppression demandent toujours une confirmation
- Les messages de succès/erreur s'affichent en haut de la page

### Navigation
- Utilisez le bouton **"Retour"** du navigateur pour revenir en arrière
- Les formulaires conservent vos données de recherche après une action

### Sécurité
- Déconnectez-vous toujours après utilisation
- Les mots de passe sont automatiquement chiffrés
- Certaines actions nécessitent des permissions spécifiques

### Performance
- La recherche est optimisée pour de grandes quantités de données
- Les filtres se combinent pour des recherches précises
- L'interface est responsive et fonctionne sur mobile
