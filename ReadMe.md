# Selenide AQA Yandex Market Mobile Phone category test suite
Automated tests intended to check whether Mobile Phones category is navigatable to and has a working brand filtering

## Tech Stack
Java
Selenide
JUnit 5
AssertJ
Allure
Maven

## Misc features
JavaDocs (written in Russian)
Tests parametrized via external config.properties file
Tests and POs avoid use of sleep() and try/catches

## Note
Idiomatically checkForNewResults() on YandexMobilePhonesCategoryPage would return an exception. 
However, given that the test failing on this step would obscure the actual test results combined with the fact that 
Yandex Market frequently produces UI-breaking bugs on high page count, an-exception-throwing check via wait/await/should
was subbed out in favor for a boolean.