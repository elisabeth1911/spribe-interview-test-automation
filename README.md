# spribe-interview-test-automation

API test framework for the Spribe interview task.

## Stack
- Java 11
- REST Assured
- TestNG
- Maven Wrapper
- Allure

## Run tests locally
Default run:
```bash
./mvnw test
```

Custom base URL and thread count:
```bash
./mvnw test -DbaseUri=http://3.68.165.45 -Dthread.count=1
```

## Allure report
Allure results are written to `allure-results/` after a test run.

Generate a static report:
```bash
./mvnw allure:report
```

Open the report:
`target/site/allure-maven-plugin/index.html`
