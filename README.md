# RestAssured

A Java 11 and RestAssured [test framework]

### Run all Test:

```
mvn clean test
```

### Run UI test suite at dev environment and reportportal enable:
```
mvn clean test -Dsuite=dev-sampleUI -Drp.enable=true -Drp.launch=UI_Test
```

### Run API test suite at dev environment and reportportal disable:
```
mvn clean test -Dsuite=sample
```
### Check the API test surefire reports:

```
target/surefire-reports/index.html
target/surefire-reports/emailable-report.html
target/surefire-reports/Automation Test Suite for APIs/Regression Tests APIs.html
```
