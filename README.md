# Project Rewards (demo)

Minimal Spring Boot demo for user points, purchases and rewards.

Prerequisites
- Java 21+ (set JAVA_HOME or have java on PATH)
- Use the included Maven wrapper (`./mvnw`)
- Docker (optional — recommended for local Postgres)

Build
```bash
./mvnw -DskipTests=true clean package
```

Run (H2 - fastest)
```bash
# start app with in-memory H2 (no external DB)
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```
App URL: http://localhost:8080


API examples
- POST /earn
```bash
curl -i -X POST http://localhost:8080/earn \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"pointsEarned":100,"purchaseId":11 }'
```
- POST /redeem
```bash
curl -i -X POST http://localhost:8080/redeem \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"pointsRedeemed":20}'
```
- GET /balance/{userId}
```bash
curl -i http://localhost:8080/balance/1
```

Files of interest
- `src/main/resources/data.sql` — H2 initializers
- `src/main/java/com/projectrewards/demo` — application source code

---
Concise README with build and run instructions.
