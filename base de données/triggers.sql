DELIMITER $$

CREATE TRIGGER trg_insert_personnel
AFTER INSERT ON personnel
FOR EACH ROW
BEGIN
    IF NEW.role_personnel = 'chef_reception' THEN
        INSERT INTO chef_reception (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'agent_securite' THEN
        INSERT INTO agent_securite (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'guide' THEN
        INSERT INTO guide (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'standardiste' THEN
        INSERT INTO standardiste (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'caissier' THEN
        INSERT INTO caissier (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'receptionniste' THEN
        INSERT INTO receptionniste (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'bagagiste' THEN
        INSERT INTO bagagiste (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'voiturier' THEN
        INSERT INTO voiturier (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'consierge' THEN
        INSERT INTO consierge (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'gouvernante_generale' THEN
        INSERT INTO gouvernante_generale (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'gouvernante' THEN
        INSERT INTO gouvernante (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'valet_chambre' THEN
        INSERT INTO valet_chambre (id_personnel) VALUES (NEW.id_personnel);
    END IF;
    IF NEW.role_personnel = 'femme_chambre' THEN
        INSERT INTO femme_chambre (id_personnel) VALUES (NEW.id_personnel);
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_insert_personne
AFTER INSERT ON personne
FOR EACH ROW
BEGIN
    IF NEW.status_personne = 'client' THEN
        INSERT INTO client (id_personne) VALUES(NEW.id_personne);
    END IF;
    IF NEW.status_personne = 'occupant' THEN
        INSERT INTO client (id_personne) VALUES(NEW.id_personne);
    END IF;
END$$

DELIMITER ;