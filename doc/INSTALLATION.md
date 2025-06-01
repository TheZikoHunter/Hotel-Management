# Manuel d'installation
## 1. Prérequis système
- Debian 12 ou supérieur - Windows 7 ou supérieur
- JDK 21
- Maven
- MySQL ou MariaDB (pour Debian)
- Git
## 2. Installation des dépendances
### a. JDK 21
#### Pour Debian
```bash
sudo apt install openjdk-21-jdk
java -version
```
#### Pour Windows
Télécharger le sur [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
### b. Maven
#### Pour Debian
```bash
sudo apt install maven
mvn -version
```
#### Pour Windows
Télécharger le sur [Maven](https://maven.apache.org/download.cgi)

Remarques : 
* sur Windows, vous pourriez avoir besoin de l'ajouter au votre PATH (variables d'environnement) ;
* Si vous ne voulez pas installer Maven, vous pouvez utiliser un IDE de Java (Eclipse ou Intelij sont recommendés)

### MySQL

MariaDB (Alternative de MySQL sur Debian)

```bash
sudo apt install mariadb-server
```
Lancer le serveur :
```bash
sudo systemctl start mariadb
sudo systemctl enable mariadb
sudo mariadb


### d. Git

- Git doit être installé et configuré ;
- Créer un dossier et se déplacer avec votre terminal dedans ;
- Cloner le projet :
```Git
git clone git@github.com:TheZikoHunter/Hotel-Management.git
```

## Configuration de la base de données
1. Créer une base de données et un utilisateur MySQL (utiliser le terminal ou un éditeur de bases de données - recommendation DBeaver):
```sql
CREATE DATABASE hotel;
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin_pass';
GRANT ALL PRIVILEGES ON hotel.* TO 'admin'@'localhost';
```
2. Exécuter le script dans le répertoire src/main/resources/database_setup.sql.
3. Créer un fichier src/main/recources/database.properties (utilisez un IDE ou un éditeur de text, sinon activer l'affichage d'extension du fichier si vous êtes sur Windows) avec le contenu suivant :
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=admin
db.password=admin_pass
```