# brekquater-spring-boot-template

A ready-to-use Spring Boot 4 REST API with Swagger UI and built-in API design feedback.

It includes Spring MVC, OpenAPI documentation with Swagger UI, and the [Brekquater API starter](https://starter.brekquater.com/api/), which shows API design warnings and a maturity score inside Swagger UI while you develop. The sample API is small on purpose and passes every rule, so you can copy its shape.

## Use the template

1. Click **Use this template** on GitHub, then **Create a new repository**.
2. In your copy, rename the package `com.brekquater.template`, the class `TemplateApplication`, and the `artifactId`, `name` and `description` in `pom.xml`.
3. Replace the `notes` package with your own API, and the placeholder title and contact in `api/ApiDocumentation.java`.

## Run it

You need a JDK 17 or later. Maven comes from the wrapper.

```bash
./mvnw spring-boot:run
```

Open `http://localhost:8080/swagger-ui.html` and click **API maturity** under the title. The panel shows only in the `dev` and `local` profiles; this template defaults to `dev`, so it shows when you run locally. A deployed service has no `dev` profile, so the panel is off there.

The sample API:

| Method and path | What it does |
|---|---|
| `GET /notes?page=0&size=20` | Lists notes, one page at a time |
| `GET /notes/{id}` | Finds one note |
| `POST /notes` | Adds a note |
| `DELETE /notes/{id}` | Deletes a note |

Input is validated with Bean Validation (`@Valid`, `@NotBlank`, `@Min`, `@Max`), and every error, including Spring's own, is an RFC 9457 problem detail (`application/problem+json`).

## Build and test

```bash
./mvnw verify
```

This runs the tests and writes the JaCoCo coverage report. After it, the panel also shows line and branch coverage and the test results, because the starter reads the files your build writes. The tests include `ApiQualityTest`, which fails when a change adds a finding from the starter, so the API's design stays clean in CI.

To see known vulnerabilities in the panel too, run [OSV-Scanner](https://google.github.io/osv-scanner/) and write its JSON report to `target/osv-scanner.json`. See [build reports](https://starter.brekquater.com/api/reports/).

## Make the build fail on blocker findings

Uncomment `brekquater.api.strict: true` in `src/main/resources/application.yml`. The application then refuses to start while any blocker finding exists, so a `@SpringBootTest` fails in CI. See the [configuration reference](https://starter.brekquater.com/api/configuration/).

## Before you deploy

- The sample API has **no authentication**. `PublicOperations` marks every operation as public on purpose. When you add Spring Security, delete that class and declare a security scheme instead; see rule [BQ-SEC-001](https://starter.brekquater.com/api/rules/BQ-SEC-001).
- The notes are kept in memory, so they vanish on restart. Replace `NoteStore` with a database.
- `pom.xml` pins Jackson above the version Spring Boot 4.1.2 manages, because of CVE-2026-110879. Delete those two properties once your Spring Boot version manages Jackson 3.1.8 and 2.21.8 or newer.

## Licence

Apache-2.0. See [LICENSE](LICENSE).
