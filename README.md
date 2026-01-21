# MedHead – Backend (Spring Boot) – PoC

Backend de la preuve de concept (PoC) MedHead.

Le service recommande un hôpital en situation d’urgence selon :
- la spécialité demandée
- les lits disponibles
- une distance routière simulée (matrice mock, sans API externe)

---

## Prérequis

- Java 21 (ou Java 17)
- Maven ou Maven Wrapper (mvnw)

---

## Lancer l’application

Depuis la racine du projet.

### Option 1 – Maven Wrapper (recommandé)

    ./mvnw spring-boot:run

### Option 2 – Maven installé

    mvn spring-boot:run

L’API démarre sur :

    http://localhost:8080

---

## Endpoints

### GET /health

Vérifie que le service fonctionne.

    curl http://localhost:8080/health

---

### GET /specialities

Retourne la liste des spécialités (PoC).

    curl http://localhost:8080/specialities

---

### GET /hospitals

Retourne la liste des hôpitaux depuis `hospitals.json`.

    curl http://localhost:8080/hospitals

---

### POST /recommendations

Recommande un hôpital selon :
- spécialité
- lits disponibles
- distance routière simulée (`distance_matrix.json`)

**Body attendu :**
- speciality (string)
- originZone (string)  
  Exemples : LONDON_CENTRAL, LONDON_EAST, LONDON_SOUTH

**Exemple :**

    curl -X POST http://localhost:8080/recommendations \
      -H "Content-Type: application/json" \
      -d '{"speciality":"Cardiologie","originZone":"LONDON_CENTRAL"}'

---

## Données PoC

Données fictives stockées dans :
- src/main/resources/hospitals.json
- src/main/resources/distance_matrix.json

La matrice simule une distance réellement parcourable sans API externe.  
En production, elle serait remplacée par un service de routage réel.

---

## Tests

Le projet inclut des **tests unitaires** et des **tests d’intégration légers**, adaptés au périmètre de la preuve de concept (PoC).

### Objectifs
- Valider la logique métier de calcul de distance (matrice simulée)
- Valider la logique de recommandation d’hôpital
- Vérifier le fonctionnement des endpoints REST principaux

### Types de tests

#### Tests unitaires
- `DistanceMatrixService` : validation du calcul de distance simulée
- Vérification des règles de sélection (spécialité, lits disponibles)

#### Tests d’intégration
- Tests des endpoints REST via le contrôleur de recommandations
- Vérification des réponses HTTP et des messages retournés

### Lancement des tests
```bash
./mvnw test

#### CI/CD

- À venir :
- pipeline build + tests

