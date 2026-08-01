# LoanConnect API (Spring Boot backend)

Backend for the LoanConnect website. Its main job: receive the "Apply for a
Loan" form submission and save it to a database.

## Stack
- Java 17
- Spring Boot 3.3 (Web, Data JPA, Validation)
- H2 database (file-based, zero setup — swap for MySQL/Postgres later, see `application.properties`)
- Maven

## Project layout
```
src/main/java/com/loanconnect/api/
  LoanConnectApplication.java      Spring Boot entry point
  controller/                      REST controllers (HTTP layer)
  service/                         Business logic
  repository/                      Spring Data JPA repositories (DB access)
  entity/                          JPA entities (DB tables)
  dto/                             Request/response shapes for the API
  config/                          CORS configuration
  exception/                       Centralized error handling
src/main/resources/application.properties
```

## Run it

```bash
cd loanconnect-backend
mvn spring-boot:run
```

The API starts on **http://localhost:8080**. Saved applications live in
`./data/loanconnect.mv.db` and survive restarts. Browse the data directly at
**http://localhost:8080/h2-console** (JDBC URL:
`jdbc:h2:file:./data/loanconnect`, user `sa`, no password).

No local MySQL/Postgres install needed to try this out — H2 handles it.

## API reference

### `POST /api/loan-applications`
Saves a new loan application. This is what the frontend's "Submit
Application" button calls.

**Request body:**
```json
{
  "fullName": "Ravi Sharma",
  "mobile": "9876543210",
  "email": "ravi@example.com",
  "dob": "1992-04-15",
  "city": "Bengaluru",
  "loanType": "Home Loan",
  "employmentType": "Salaried",
  "companyName": "Acme Pvt Ltd",
  "monthlyIncome": 65000,
  "loanAmount": 2500000,
  "loanTenure": 15,
  "loanPurpose": "Purchase of a 2BHK apartment",
  "aadhaarFileName": "aadhaar.pdf",
  "panFileName": "pan.pdf",
  "salarySlipFileName": "salary_slip.pdf",
  "termsAccepted": true
}
```

**Success — `201 Created`:**
```json
{
  "id": 1,
  "applicationRef": "APP-20260801-0001",
  "fullName": "Ravi Sharma",
  "email": "ravi@example.com",
  "loanType": "Home Loan",
  "loanAmount": 2500000,
  "status": "SUBMITTED",
  "submittedAt": "2026-08-01T10:15:30"
}
```

**Validation failure — `400 Bad Request`:**
```json
{
  "timestamp": "2026-08-01T10:15:30",
  "status": 400,
  "error": "Validation failed",
  "fieldErrors": {
    "mobile": "Mobile number must be exactly 10 digits",
    "email": "Enter a valid email address"
  }
}
```

### `GET /api/loan-applications`
Returns every saved application (for an admin dashboard, if you build one later).

### `GET /api/loan-applications/{id}`
Fetch one application by its database id.

### `GET /api/loan-applications/ref/{applicationRef}`
Fetch one application by its reference number, e.g. `APP-20260801-0001`.

### `DELETE /api/loan-applications/{id}`
Removes an application.

## Connecting the existing frontend

`script.js` has already been updated to `fetch()` this endpoint instead of
(well, in addition to) `localStorage`. Two things to check:

1. **Run the frontend through a local server, not `file://`.** Opening
   `index.html` directly causes the browser to send `Origin: null`, which
   most browsers block for `fetch()` calls. Easiest options:
   - VS Code "Live Server" extension, or
   - `python3 -m http.server 5500` from the frontend folder, then visit
     `http://localhost:5500`.
2. **The API base URL** is set at the top of `script.js`:
   ```js
   const API_BASE_URL = 'http://localhost:8080/api';
   ```
   Update this if you deploy the backend elsewhere.

CORS is already open for local development (`WebConfig.java`). Restrict
`allowedOriginPatterns` to your real frontend domain before going to production.

## Document uploads (Aadhaar / PAN / Salary Slip)

Right now the API only stores the **file name** of each upload, matching
what the frontend already sent to `localStorage`. The actual file bytes
never leave the browser. To store real files:

1. Change the endpoint to accept `multipart/form-data` instead of JSON
   (`@RequestPart` for each file, `@RequestPart` for the JSON fields).
2. Save files to disk, or better, to object storage (S3-compatible), and
   store the resulting URL/path in the entity instead of just a file name.
3. Update `script.js` to build a `FormData` object instead of `JSON.stringify(...)`.

Happy to wire this up if you want real file storage next.

## Switching from H2 to MySQL

See the commented block at the bottom of `application.properties` — it's a
two-step change (add the driver dependency, swap four datasource lines).
No Java code changes needed.
