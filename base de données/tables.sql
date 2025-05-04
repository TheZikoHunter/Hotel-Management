CREATE TABLE personne (
    id_personne INT AUTO_INCREMENT PRIMARY KEY,
    nom_personne VARCHAR(50),
    prenom_personne VARCHAR(50),
    tel_personne VARCHAR(20),
    email_personne VARCHAR(100),
    dn_personne DATE,
    sexe_personne ENUM('masculin', 'feminin'),
    nat_personne VARCHAR(30),
    status_personne ENUM('client', 'occupant')
);

CREATE TABLE client (
    id_personne INT PRIMARY KEY,
    id_client INT UNIQUE,
    FOREIGN KEY (id_personne) REFERENCES personne(id_personne)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE occupant (
    id_personne INT PRIMARY KEY,
    id_occupant INT UNIQUE,
    FOREIGN KEY (id_personne) REFERENCES personne(id_personne)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE service(
    id_service INT AUTO_INCREMENT PRIMARY KEY,
    nom_service VARCHAR(20)
);

CREATE TABLE personnel(
    id_personnel INT AUTO_INCREMENT PRIMARY KEY,
    nom_personnel VARCHAR(30),
    pernom_personnel VARCHAR(30),
    dn_personnel DATE,
    role_personnel ENUM('chef_reception', 'agent_securite', 'guide', 'standardiste', 
                        'caissier', 'receptionniste', 'bagagiste', 'voiturier', 'consierge',
                        'gouvernante_generale', 'gouvernante', 'valet_chambre', 'femme_chambre'),
    est_disponible INT, --'0' ou '1'
    nom_utilisateur VARCHAR(40),
    mot_de_passe VARCHAR(100),
    id_service INT,
    FOREIGN KEY id_service REFERENCES service(id_service)
);

CREATE TABLE chef_reception(
    id_personnel INT,
    id_chef_reception INT UNIQUE,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE agent_securite(
    id_personnel INT,
    id_agent_securite INT UNIQUE,
    id_chef_reception INT,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_chef_reception) REFERENCES chef_reception(id_chef_reception)
);

CREATE TABLE guide(
    id_personnel INT,
    id_guide INT UNIQUE,
    id_chef_reception INT,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_chef_reception) REFERENCES chef_reception(id_chef_reception)
);

CREATE TABLE standardiste(
    id_personnel INT,
    id_standardiste INT UNIQUE,
    id_chef_reception INT,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_chef_reception) REFERENCES chef_reception(id_chef_reception)
);


CREATE TABLE caissier(
    id_personnel INT,
    id_caissier INT UNIQUE,
    id_chef_reception INT,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_chef_reception) REFERENCES chef_reception(id_chef_reception)
);

CREATE TABLE receptionniste (
    id_personnel INT,
    id_receptionniste INT UNIQUE,
    id_chef_reception INT,
    PRIMARY KEY (id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_chef_reception) REFERENCES chef_reception(id_chef_reception)
);

CREATE TABLE bagagiste(
    id_personnel INT,
    id_bagagiste INT UNIQUE,
    id_receptionniste INT,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_receptionniste) REFERENCES receptionniste(id_receptionniste)
);

CREATE TABLE voiturier(
    id_personnel INT,
    id_voiturier INT UNIQUE,
    id_receptionniste INT,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_receptionniste) REFERENCES receptionniste(id_receptionniste)
);

CREATE TABLE consierge(
    id_personnel INT,
    id_consierge INT UNIQUE,
    id_receptionniste INT,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_receptionniste) REFERENCES receptionniste(id_receptionniste)
);

CREATE TABLE reservation (
    id_reservation AUTO_INCREMENT PRIMARY KEY,
    date_reservation DATE,
    debut_sejours DATE,
    fin_sejours DATE,
    nombre_adultes INT,
    nombre_enfants INT,
    petit_dejouner_inclus ENUM('oui', 'non'),
    etat VARCHAR(10),
    id_client INT,
    id_receptionniste INT,
    FOREIGN KEY (id_client) REFERENCES client(id_client)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_receptionniste) REFERENCES receptionniste(id_receptionniste)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE chambre (
    num_chambre PRIMARY KEY,
    type_chambre ENUM('single', 'double1lit', 'double2lit', 'triple3lit', 'triple2lit', 'suite', 'calme'),
    est_netoyee INT, --'0' ou '1'
    etage INT
);

CREATE TABLE occupant_reservation(
    id_reservation INT,
    id_occupant INT,
    PRIMARY KEY(id_reservation,id_occupant),
    FOREIGN KEY (id_reservation) REFERENCES reservation(id_reservation)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_occupant) REFERENCES occupant(id_occupant)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE chambre_reservation(
    id_reservation INT,
    num_chambre INT,
    PRIMARY KEY(id_reservation,num_chambre),
    FOREIGN KEY (id_reservation) REFERENCES reservation(id_reservation)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (num_chambre) REFERENCES chambre(num_chambre)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE demande(
    id_demande INT AUTO_INCREMENT PRIMARY KEY,
    temps_de_demande TIME,
    num_chambre INT,
    id_service INT,
    FOREIGN KEY num_chambre REFERENCES chambre(num_chambre)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY id_service REFERENCES service(id_service)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE tache(
    id_tache INT PRIMARY KEY,
    etat_tache VARCHAR(10),
    temps_debut TIME,
    temps_completion TIME,
    id_demande INT,
    id_personnel INT,
    FOREIGN KEY id_demande REFERENCES demande(id_demande),
    FOREIGN KEY id_personnel REFERENCES personnel(id_personnel)
);

CREATE TABLE gouvernante_generale(
    id_personnel INT,
    id_gouvernante_generale INT UNIQUE,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE gouvernante(
    id_personnel INT,
    id_gouvernante INT UNIQUE,
    id_gouvernante_generale INT,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY(id_gouvernante_generale) REFERENCES gouvernante_generale(id_gouvernante_generale)
);

CREATE TABLE valet_chambre(
    id_personnel INT,
    id_valet_chambre INT UNIQUE,
    id_gouvernante INT,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY(id_gouvernante) REFERENCES gouvernante(id_gouvernante)
);

CREATE TABLE femme_chambre(
    id_personnel INT,
    id_femme_chambre INT UNIQUE,
    id_gouvernante INT,
    PRIMARY KEY(id_personnel),
    FOREIGN KEY (id_personnel) REFERENCES personnel(id_personnel)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY(id_gouvernante) REFERENCES gouvernante(id_gouvernante)
);