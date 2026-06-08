# AQA Java Selenide тест работы поисковой строки ya.ru
Проверяет три способа ввода поискового запроса (параметризирован) и сверяет что открытая страница соответствует ему.

## Стек
Java
Selenide
JUnit 5
AssertJ
Allure
Maven

## Дополнительные фичи
JavaDocs-документация на русском
Тесты параметризированны через внешний config.properties file
Тесты принимают кириллицу (в IDEA нужно включить Transparent native-to-ascii conversion в  File Encodings)