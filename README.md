# Movie Reviewer - Proiect Programare Web Java

**Movie Reviewer** este o aplicație web completă de tip **Full Stack** pentru gestionarea și recenzierea filmelor, construită cu **Java Spring Boot** pe backend și **React** pe frontend. Aplicația suportă autentificare JWT, gestiune de filme, recenzii, watchlist și se poate rula și în cloud pe infrastructură Google Cloud (Cloud Run, Cloud SQL, Cloud Storage).

---

## 🎯 Business Requirements

1. **User Registration & Authentication**  
   Utilizatorii se pot înregistra și autentifica, primind token JWT stocat în cookie-uri pentru sesiuni sigure.

2. **Movie Management**  
   Utilizatorii pot adăuga, edita, șterge și vizualiza filmele dorite prin adaugarea acestora intr-un watchlist, fiecare cu gen, actori, postere și detalii suplimentare preluate prin OMDB API.

3. **Review System**  
   Utilizatorii pot lăsa recenzii și rating-uri la fiecare film, vizibile public.

4. **Watchlist**  
   Utilizatorii pot salva filme într-un watchlist personal.

5. **Genre & Actor Management**  
   Adminii pot adăuga sau edita genuri și actori.

6. **Role-based Access Control**  
   Două roluri:  
   - **Admin:** Control complet asupra aplicației  
   - **User:** Funcționalități de utilizator obișnuit

7. **Movie Filtering & Search**  
   Utilizatorii pot filtra și căuta filme după titlu, gen, actor.

8. **Secure API**  
   Accesul la operațiuni sensibile e protejat prin JWT și Spring Security.

9. **Audit & Logging**  
   Aplicația loghează acțiunile critice în fișiere proprii.

10. **Cloud Ready**  
    Aplicația rulează local sau pe Google Cloud cu Cloud SQL, Cloud Storage și Cloud Run.

---

## 🚀 Minimum Viable Product (MVP)

### 1. Autentificare și înregistrare

- Înregistrare cont nou cu email, parolă, rol.
- Login cu generare JWT, sesiune stocată în cookie.
- Logout și management sesiune.

### 2. Gestiune filme și recenzii

- Adăugare, editare, ștergere film (admin).
- Vizualizare filme cu postere și detalii.
- Recenzii și rating-uri pentru fiecare film.
- Watchlist personal pentru fiecare utilizator.

### 3. Gestiune genuri și actori

- Adăugare/ștergere/actualizare genuri și actori (admin).
- Filtrare filme după gen sau actor.

### 4. Securitate și logging

- Protejarea endpoint-urilor cu JWT.
- Log-uri pentru acțiuni importante.

---

## ☁️ Cloud Integration

- **Cloud SQL** pentru MySQL (persistent storage).
- **Cloud Storage** pentru hostare frontend React build.
- **Cloud Run** pentru deploy backend Spring Boot containerizat (Docker).
- Frontend-ul React se poate servi din cloud storage sau separat, interacționând cu backend-ul Spring.

---

## 🏗️ Tehnologii folosite

- **Backend:** Java 17, Spring Boot, Spring Security, JPA, Hibernate, MySQL, JWT (io.jsonwebtoken)
- **Frontend:** React 19, Vite, TailwindCSS, Bootstrap, Axios, React Router
- **Dev Tools:** Lombok, JUnit, Spring Boot Test, ESLint, TypeScript
- **Cloud:** Google Cloud Run, Cloud SQL, Cloud Storage, Docker

---

## 🧪 Testing

- **Integration Tests** pentru Spring Boot: testare workflow complet (API endpoints, DB, securitate, recenzii, watchlist etc.).
- **Frontend:** Teste basic, linters pentru code quality.

---

## 📦 Structură repozitoriu

- `/backend` - Spring Boot application (Java)
- `/frontend` - React app (TypeScript)

---

## ⚙️ Installation & Setup

### 1. Clone repository

```bash
git clone https://github.com/your-username/movie-reviewer.git
cd movie-reviewer
