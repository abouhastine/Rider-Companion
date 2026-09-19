# Rider Companion — Spécifications fonctionnelles et techniques

## 1. Présentation du projet

**Rider Companion** est une application destinée aux motards. Elle vise à centraliser les informations essentielles liées au profil du pilote, à ses motos, à leur entretien et à la préparation de ses sorties.

La première version du produit doit servir de base technique et fonctionnelle réutilisable après le stage.

Le périmètre couvre uniquement :

- une **Web App** ;
- une **Android App** ;
- une **REST API** commune ;
- une base de données centralisée ;
- un **Dashboard** ;
- un **Virtual Garage** ;
- un **Maintenance Logbook** ;
- un **Ride Planner** simplifié.

Les fonctionnalités avancées telles que la navigation GPS, la météo, les services communautaires et l’Intelligence Artificielle sont hors périmètre de cette première version.

---

## 2. Objectifs du stage

Le stage dure environ un mois.

Les objectifs sont les suivants :

1. Concevoir une architecture technique propre et évolutive.
2. Développer une **REST API** commune à la **Web App** et à l’**Android App**.
3. Permettre à un utilisateur de créer un compte et de gérer son profil.
4. Permettre à un utilisateur d’enregistrer une ou plusieurs motos.
5. Permettre à un utilisateur de suivre les opérations d’entretien.
6. Afficher un **Dashboard** synthétique.
7. Permettre de préparer une sortie moto simple.
8. Produire une documentation facilitant la reprise du projet.

---

## 3. Périmètre du MVP

Le **Minimum Viable Product (MVP)** comprend les modules suivants :

- **Authentication & Rider Profile** ;
- **Dashboard** ;
- **Virtual Garage** ;
- **Maintenance Logbook** ;
- **Ride Planner**.

---

## 4. Utilisateurs ciblés

La première version s’adresse principalement :

- aux motards débutants ;
- aux titulaires d’un permis A1, A2 ou A ;
- aux utilisateurs possédant une ou plusieurs motos ;
- aux motards souhaitant suivre leurs entretiens et préparer leurs sorties.

Un seul rôle est prévu dans le MVP :

- `USER`.

Le rôle `ADMIN` pourra être ajouté ultérieurement.

---

## 5. Plateformes

### 5.1 Web App

La **Web App** doit être **responsive** et utilisable sur :

- ordinateur ;
- tablette ;
- navigateur mobile.

Elle doit proposer toutes les fonctionnalités du MVP.

### 5.2 Android App

L’**Android App** doit proposer au minimum :

- l’inscription ;
- la connexion ;
- le **Dashboard** ;
- la consultation et la gestion des motos ;
- la consultation et l’ajout des entretiens ;
- la préparation d’une sortie.

L’**Android App** consomme la même **REST API** que la **Web App**.

---

## 6. Module Authentication & Rider Profile

### 6.1 Sign Up

L’utilisateur doit pouvoir créer un compte avec :

- prénom ;
- nom ;
- adresse e-mail ;
- mot de passe ;
- confirmation du mot de passe.

### 6.2 Sign In

L’utilisateur doit pouvoir se connecter avec :

- adresse e-mail ;
- mot de passe.

### 6.3 Sign Out

L’utilisateur doit pouvoir fermer sa session depuis la **Web App** et l’**Android App**.

### 6.4 Rider Profile

L’utilisateur doit pouvoir renseigner :

- prénom ;
- nom ;
- type de permis ;
- année d’obtention ;
- niveau de conduite ;
- usage principal ;
- distance annuelle estimée.

Valeurs possibles :

#### License Type

- `A1`
- `A2`
- `A`

#### Experience Level

- `BEGINNER`
- `INTERMEDIATE`
- `EXPERIENCED`

#### Primary Usage

- `COMMUTING`
- `LEISURE`
- `TRAVEL`
- `MIXED`

### 6.5 Business Rules

- Une adresse e-mail ne peut être associée qu’à un seul compte.
- Le mot de passe doit contenir au minimum huit caractères.
- Un utilisateur connecté ne peut accéder qu’à ses propres données.
- Une session expirée doit rediriger l’utilisateur vers l’écran de connexion.

---

## 7. Module Dashboard

Le **Dashboard** constitue l’écran principal après la connexion.

### 7.1 Informations affichées

#### User Summary

- prénom ;
- niveau de conduite ;
- nombre total de motos.

#### Primary Motorcycle

- marque ;
- modèle ;
- année ;
- kilométrage actuel ;
- photo si disponible.

#### Maintenance Summary

- dernier entretien enregistré ;
- prochain entretien prévu ;
- nombre d’entretiens à venir ;
- nombre d’entretiens en retard.

#### Statistics

- nombre total d’entretiens ;
- coût total des entretiens ;
- nombre de sorties préparées ;
- distance totale estimée des sorties.

#### Quick Actions

- `Add Motorcycle`
- `Add Maintenance Record`
- `Plan Ride`
- `Open Garage`

### 7.2 Empty States

Si aucune moto n’est enregistrée :

> Vous n’avez pas encore ajouté de moto. Ajoutez votre première moto pour commencer.

Si aucun entretien n’est enregistré :

> Aucun entretien enregistré pour le moment.

### 7.3 Android Layout

Sur Android, le **Dashboard** peut être affiché sous forme de cartes verticales.

La **Bottom Navigation** peut contenir :

- `Home`
- `Garage`
- `Maintenance`
- `Rides`

---

## 8. Module Virtual Garage

### 8.1 Motorcycle List

L’utilisateur doit pouvoir consulter la liste de ses motos.

Chaque élément affiche :

- marque ;
- modèle ;
- année ;
- kilométrage ;
- photo ;
- statut de moto principale.

### 8.2 Add Motorcycle

Champs attendus :

- marque ;
- modèle ;
- année ;
- cylindrée ;
- puissance ;
- type de carburant ;
- immatriculation facultative ;
- date d’achat facultative ;
- kilométrage actuel ;
- consommation moyenne facultative ;
- photo facultative ;
- statut de moto principale.

### 8.3 Edit Motorcycle

L’utilisateur doit pouvoir modifier les données d’une moto.

### 8.4 Delete Motorcycle

La suppression doit nécessiter une confirmation.

> Êtes-vous sûr de vouloir supprimer cette moto ? Les données associées pourront être supprimées.

Une évolution ultérieure pourra remplacer la suppression par une fonctionnalité `Archive Motorcycle`.

### 8.5 Primary Motorcycle

- Une seule moto principale est autorisée par utilisateur.
- La sélection d’une nouvelle moto principale retire automatiquement ce statut à l’ancienne.

### 8.6 Update Mileage

Le kilométrage doit pouvoir être mis à jour depuis :

- la fiche moto ;
- le **Dashboard**.

### 8.7 Business Rules

- Le kilométrage doit être supérieur ou égal à zéro.
- L’année doit être cohérente.
- La cylindrée doit être supérieure à zéro.
- Une moto doit obligatoirement être rattachée à un utilisateur.
- Un utilisateur ne peut consulter et modifier que ses propres motos.

---

## 9. Module Maintenance Logbook

### 9.1 Maintenance Record List

Chaque entretien affiche :

- type ;
- date ;
- kilométrage ;
- coût ;
- statut ;
- prestataire.

### 9.2 Maintenance Types

- `OIL_CHANGE`
- `OIL_FILTER`
- `AIR_FILTER`
- `CHAIN_KIT`
- `CHAIN_LUBRICATION`
- `CHAIN_TENSION`
- `TIRES`
- `BRAKE_PADS`
- `BRAKE_FLUID`
- `COOLANT`
- `SPARK_PLUGS`
- `BATTERY`
- `GENERAL_SERVICE`
- `OTHER`

### 9.3 Add Completed Maintenance Record

Champs :

- moto ;
- type d’entretien ;
- date de réalisation ;
- kilométrage ;
- coût ;
- prestataire ;
- notes ;
- prochaine échéance en date ;
- prochaine échéance en kilométrage.

### 9.4 Schedule Maintenance

Champs :

- type ;
- date prévue ;
- kilométrage prévu ;
- commentaire.

### 9.5 Maintenance Status

- `COMPLETED`
- `PLANNED`
- `OVERDUE`

Un entretien est `OVERDUE` lorsque :

- la date prévue est dépassée ;
- ou le kilométrage prévu est atteint ou dépassé.

### 9.6 Alerts

Le **Dashboard** doit afficher :

- les entretiens prévus dans les 30 prochains jours ;
- les entretiens prévus dans les 1 000 prochains kilomètres ;
- les entretiens en retard.

Les **Push Notifications** sont hors périmètre du MVP.

### 9.7 Maintenance Costs

L’application doit calculer :

- le coût total pour une moto ;
- le coût total pour l’ensemble du garage ;
- le coût moyen par entretien.

### 9.8 Business Rules

- Un entretien doit être associé à une moto.
- La moto doit appartenir à l’utilisateur connecté.
- Le coût doit être supérieur ou égal à zéro.
- La date de réalisation ne doit pas être située dans le futur.
- Un kilométrage incohérent doit générer un avertissement.

---

## 10. Module Ride Planner

Ce module ne fournit pas encore de navigation GPS.

### 10.1 Create Ride

Champs :

- titre ;
- moto ;
- date ;
- heure de départ ;
- lieu de départ ;
- destination ;
- distance estimée ;
- durée estimée ;
- type de trajet ;
- autoroute autorisée ou non ;
- péages autorisés ou non ;
- nombre de pauses ;
- notes.

### 10.2 Ride Types

- `LEISURE_RIDE`
- `COMMUTE`
- `ROAD_TRIP`
- `BUSINESS_TRIP`
- `GROUP_RIDE`
- `OTHER`

### 10.3 Ride Checklist

Une checklist standard doit être générée :

- vérifier la pression des pneus ;
- vérifier le niveau de carburant ;
- vérifier la chaîne ;
- vérifier les feux ;
- vérifier la météo ;
- prendre les documents ;
- prendre le téléphone ;
- prévoir de l’eau ;
- vérifier l’équipement.

Chaque item peut être coché ou décoché.

### 10.4 Ride Status

- `DRAFT`
- `PLANNED`
- `COMPLETED`
- `CANCELLED`

### 10.5 Complete Ride

Champs facultatifs à la fin d’une sortie :

- distance réelle ;
- durée réelle ;
- coût carburant ;
- commentaire ;
- note sur cinq.

### 10.6 Ride Views

L’utilisateur doit pouvoir consulter :

- les sorties à venir ;
- les sorties passées ;
- les sorties annulées.

### 10.7 Business Rules

- Une sortie doit être associée à une moto.
- La moto doit appartenir à l’utilisateur.
- La distance doit être positive.
- La date est obligatoire pour une sortie `PLANNED`.
- La suppression d’une sortie `COMPLETED` doit demander une confirmation.

---

## 11. Navigation

### 11.1 Web App Navigation

- `Dashboard`
- `Garage`
- `Maintenance`
- `Rides`
- `Profile`
- `Sign Out`

### 11.2 Android Navigation

**Bottom Navigation** :

- `Home`
- `Garage`
- `Maintenance`
- `Rides`

Le profil peut être accessible depuis l’**App Bar**.

---

## 12. Écrans à développer

### 12.1 Web App

- `Sign In Page`
- `Sign Up Page`
- `Dashboard Page`
- `Motorcycle List Page`
- `Add Motorcycle Page`
- `Motorcycle Details Page`
- `Edit Motorcycle Page`
- `Maintenance List Page`
- `Add Maintenance Record Page`
- `Maintenance Details Page`
- `Ride List Page`
- `Create Ride Page`
- `Ride Details Page`
- `Profile Page`

### 12.2 Android App

- `Sign In Screen`
- `Sign Up Screen`
- `Dashboard Screen`
- `Motorcycle List Screen`
- `Motorcycle Details Screen`
- `Add/Edit Motorcycle Screen`
- `Maintenance List Screen`
- `Add Maintenance Record Screen`
- `Ride List Screen`
- `Create Ride Screen`
- `Ride Details Screen`
- `Profile Screen`

---

## 13. Architecture technique proposée

### 13.1 Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Liquibase
- Maven
- Swagger / OpenAPI

### 13.2 Web App

- React
- TypeScript
- Vite
- React Router
- Axios ou Fetch API
- React Hook Form
- Material UI

### 13.3 Android App

Option recommandée :

- Kotlin
- Jetpack Compose
- Retrofit
- ViewModel
- Kotlin Coroutines
- DataStore

Un cache local avec Room pourra être ajouté ultérieurement.

### 13.4 Repository Strategy

Deux options sont possibles :

#### Monorepo

```text
rider-companion/
├── backend/
├── web-app/
├── android-app/
└── docs/
```

#### Multi-repository

```text
rider-companion-backend
rider-companion-web
rider-companion-android
rider-companion-docs
```

Pour un stage court, le **monorepo** est recommandé.

---

## 14. Modèle de données simplifié

### User

```text
id
firstName
lastName
email
passwordHash
createdAt
updatedAt
```

### RiderProfile

```text
id
userId
licenseType
licenseYear
experienceLevel
primaryUsage
estimatedAnnualDistance
```

### Motorcycle

```text
id
userId
brand
model
year
engineCapacity
power
fuelType
registrationNumber
purchaseDate
currentMileage
averageConsumption
imageUrl
primaryMotorcycle
createdAt
updatedAt
```

### MaintenanceRecord

```text
id
motorcycleId
maintenanceType
status
completionDate
plannedDate
mileage
plannedMileage
cost
serviceProvider
notes
createdAt
updatedAt
```

### Ride

```text
id
userId
motorcycleId
title
plannedDate
departureTime
departureLocation
destination
estimatedDistance
actualDistance
estimatedDuration
actualDuration
rideType
useHighway
useTolls
plannedBreaks
fuelCost
status
rating
notes
createdAt
updatedAt
```

### RideChecklistItem

```text
id
rideId
label
checked
```

---

## 15. REST API indicative

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
GET  /api/users/me
PUT  /api/users/me
```

### Motorcycles

```http
GET    /api/motorcycles
GET    /api/motorcycles/{id}
POST   /api/motorcycles
PUT    /api/motorcycles/{id}
DELETE /api/motorcycles/{id}
PATCH  /api/motorcycles/{id}/mileage
PATCH  /api/motorcycles/{id}/primary
```

### Maintenance

```http
GET    /api/maintenance
GET    /api/motorcycles/{motorcycleId}/maintenance
GET    /api/maintenance/{id}
POST   /api/maintenance
PUT    /api/maintenance/{id}
DELETE /api/maintenance/{id}
```

### Rides

```http
GET    /api/rides
GET    /api/rides/{id}
POST   /api/rides
PUT    /api/rides/{id}
DELETE /api/rides/{id}
PATCH  /api/rides/{id}/status
PATCH  /api/rides/{rideId}/checklist/{itemId}
```

### Dashboard

```http
GET /api/dashboard
```

Le endpoint `/api/dashboard` doit retourner une vue agrégée.

---

## 16. Exemple de réponse Dashboard

```json
{
  "user": {
    "firstName": "Aymen",
    "experienceLevel": "BEGINNER"
  },
  "motorcyclesCount": 1,
  "primaryMotorcycle": {
    "id": 1,
    "brand": "Triumph",
    "model": "Tiger 900 GT Pro",
    "year": 2025,
    "currentMileage": 3500,
    "imageUrl": null
  },
  "maintenance": {
    "lastMaintenance": {
      "type": "GENERAL_SERVICE",
      "date": "2026-02-01",
      "mileage": 1000
    },
    "upcomingCount": 2,
    "overdueCount": 0
  },
  "statistics": {
    "maintenanceTotalCost": 320.50,
    "maintenanceCount": 4,
    "plannedRidesCount": 2,
    "estimatedRideDistance": 420
  }
}
```

---

## 17. Exigences non fonctionnelles

### 17.1 Security

- Les mots de passe ne doivent jamais être stockés en clair.
- Utiliser BCrypt pour le hash des mots de passe.
- Protéger toutes les routes métier.
- Vérifier la propriété des données côté backend.
- Valider les entrées côté client et serveur.
- Ne pas exposer les erreurs internes.
- Stocker les secrets dans des variables d’environnement.

### 17.2 Code Quality

- Utiliser Git.
- Définir une convention de branches.
- Utiliser des commits explicites.
- Séparer `Controller`, `Service`, `Repository`, `DTO` et `Entity`.
- Centraliser la gestion des erreurs.
- Ajouter des `README`.
- Configurer un `formatter` et un `linter`.

### 17.3 Responsive Design

La **Web App** doit fonctionner sur desktop, tablette et mobile.

### 17.4 Performance

- Limiter le nombre de requêtes du **Dashboard**.
- Paginer les listes si nécessaire.
- Limiter la taille des images.
- Éviter les appels réseau redondants.

### 17.5 Documentation

Le projet doit inclure :

- un `README.md` principal ;
- une procédure d’installation ;
- une procédure de lancement ;
- une description de l’architecture ;
- une documentation de la **REST API** ;
- la liste des variables d’environnement ;
- un jeu de données de démonstration.

---

## 18. Tests attendus

### Backend

Tests minimum :

- création d’un utilisateur ;
- authentification ;
- ajout d’une moto ;
- changement de moto principale ;
- calcul du statut d’entretien ;
- contrôle d’accès ;
- création d’une sortie ;
- génération de checklist ;
- récupération du **Dashboard**.

### Web App

Tests prioritaires :

- formulaire de connexion ;
- formulaire d’ajout d’une moto ;
- affichage du **Dashboard** ;
- validation des formulaires.

### Android App

Tests prioritaires :

- connexion ;
- chargement du **Dashboard** ;
- ajout d’une moto ;
- ajout d’un entretien.

---

## 19. Roadmap du stage

### Semaine 1 — Project Setup & Authentication

Objectifs :

- définir les parcours utilisateurs ;
- produire des wireframes simples ;
- créer le monorepo ;
- initialiser le backend ;
- initialiser PostgreSQL ;
- configurer Liquibase ;
- développer l’inscription et la connexion ;
- configurer Swagger / OpenAPI.

Livrables :

- architecture initiale ;
- modèle de données ;
- authentification fonctionnelle ;
- documentation de démarrage.

### Semaine 2 — Virtual Garage & Dashboard

Objectifs :

- développer le CRUD des motos ;
- développer la notion de moto principale ;
- construire le garage dans la **Web App** ;
- développer le premier **Dashboard** ;
- connecter la **Web App** au backend ;
- gérer les erreurs et validations.

Livrables :

- **Virtual Garage** fonctionnel ;
- **Dashboard** web ;
- API motos testée.

### Semaine 3 — Maintenance Logbook & Android Foundation

Objectifs :

- développer le CRUD des entretiens ;
- calculer les statuts `PLANNED` et `OVERDUE` ;
- afficher les alertes ;
- intégrer le module dans la **Web App** ;
- initialiser l’**Android App** ;
- connecter Android à la **REST API** ;
- afficher le **Dashboard** Android.

Livrables :

- **Maintenance Logbook** fonctionnel ;
- alertes d’entretien ;
- authentification Android ;
- **Dashboard** Android.

### Semaine 4 — Ride Planner, Testing & Documentation

Objectifs :

- développer le **Ride Planner** ;
- générer la checklist ;
- intégrer les sorties dans la **Web App** ;
- intégrer les écrans Android prioritaires ;
- corriger les bugs ;
- compléter les tests ;
- finaliser la documentation ;
- préparer une démonstration.

Livrables :

- **Ride Planner** fonctionnel ;
- Android App de démonstration ;
- documentation finale ;
- données de démonstration ;
- démonstration de fin de stage.

---

## 20. Priorisation

### Must Have

- Authentication
- Rider Profile
- Motorcycle CRUD
- Primary Motorcycle
- Dashboard
- Maintenance CRUD
- Maintenance Alerts
- Ride Planner
- Ride Checklist
- Responsive Web App
- Android App connectée à la REST API
- Documentation

### Should Have

- photo de moto ;
- filtres ;
- recherche ;
- graphiques simples ;
- Dark Mode ;
- cache local Android ;
- Local Notifications.

### Out of Scope

- navigation GPS ;
- génération automatique d’itinéraires ;
- météo ;
- assistant IA ;
- diagnostic mécanique par photo ;
- détection de chute ;
- suivi GPS en temps réel ;
- réseau social ;
- chat ;
- marketplace ;
- OBD ;
- paiement ;
- réservation.

---

## 21. Critères d’acceptation

Le stage est considéré comme réussi si :

- un utilisateur peut s’inscrire et se connecter ;
- il peut enregistrer une moto ;
- il peut définir une moto principale ;
- il peut consulter un Dashboard ;
- il peut enregistrer un entretien ;
- il peut consulter les entretiens à venir et en retard ;
- il peut préparer une sortie ;
- il peut utiliser une checklist ;
- les données sont accessibles depuis la Web App et l’Android App ;
- les données sont sécurisées ;
- le projet est documenté ;
- un autre développeur peut installer et reprendre le projet.

---

## 22. Évolutions futures

- Motorcycle Weather Score
- Smart Ride Recommendations
- GPS Route Generation
- Ride Tracking
- Push Notifications
- Expense Tracking
- Equipment Management
- Smart Maintenance Assistant
- AI Riding Assistant
- Road Trip Planner
- Community Features
- Connected Device Integrations


---


## Annexe — Architecture cible (hors périmètre du stage)

Cette architecture représente la cible à moyen terme. Le stage n'a pas vocation à implémenter l'ensemble de ces composants, mais le projet doit être conçu pour pouvoir les intégrer progressivement.

### Backend

- Java 21
- Spring Boot 3
- Maven
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Liquibase
- MapStruct
- Bean Validation
- OpenAPI / Swagger
- JUnit 5
- Mockito
- Testcontainers

### Web App

- React
- TypeScript
- Vite
- Material UI
- React Router
- TanStack Query
- React Hook Form
- Zod

### Android App

- Kotlin
- Jetpack Compose
- MVVM
- Hilt
- Retrofit
- Kotlin Coroutines
- DataStore

### DevOps

- Docker
- Docker Compose
- GitHub Actions
- Kubernetes
- SonarQube
- Checkstyle
- Spotless

### Architecture

- REST API
- Layered Architecture
- DTO Pattern
- Repository Pattern
- Service Layer
- Global Exception Handling
- Environment-based Configuration


### Continuous Delivery

- GitHub Actions pour les pipelines CI/CD ;
- création et publication des Docker images ;
- déploiement automatisé sur Kubernetes ;
- gestion des environnements `development`, `staging` et `production` ;
- Helm pourra être ajouté ultérieurement pour le packaging et le déploiement.

> **Note :** seuls les éléments essentiels (Java, Spring Boot, Maven, Liquibase, PostgreSQL, React, Kotlin, Jetpack Compose) sont attendus pendant le stage. Les autres technologies constituent la feuille de route technique après le stage.
