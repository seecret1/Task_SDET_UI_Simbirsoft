<h1>UI Automation Testing Project</h1>
<p class="subtitle">Автоматизированное тестирование сайта XYZ Bank</p>

<ul class="nav-list">
    <li><a href="#description">Описание</a></li>
    <li><a href="#tech-stack">Технологии</a></li>
    <li><a href="#setup">Установка</a></li>
    <li><a href="#testing">Тестирование</a></li>
    <li><a href="#reports">Отчеты</a></li>
    <li><a href="#ci-cd">CI/CD</a></li>
</ul>

<h2>Описание проекта</h2>
<p>Проект автоматизированного тестирования UI для сайта <a href="https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager" target="_blank">XYZ Bank</a></p>

<h3>Основные возможности:</h3>
<ul>
    <li>✅ Добавление клиентов с различными данными</li>
    <li>✅ Сортировка клиентов по имени</li>
    <li>✅ Удаление клиентов по имени, фамилии и почтовому коду</li>
    <li>✅ Параллельное выполнение тестов</li>
    <li>✅ Allure отчеты с детализацией</li>
    <li>✅ CI/CD интеграция</li>
</ul>

<h2>Технологический стек</h2>
<div class="tech-item">
    <h4>Java</h4>
    <p><code>17+</code></p>
    <p>Основной язык программирования</p>
</div>
<div class="tech-item">
    <h4>JUnit 5</h4>
    <p><code>5.10.0</code></p>
    <p>Фреймворк для тестирования</p>
</div>
<div class="tech-item">
    <h4>Selenium</h4>
    <p><code>4.35.0</code></p>
    <p>Автоматизация браузера</p>
</div>
<div class="tech-item">
    <h4>Maven</h4>
    <p><code>3.9+</code></p>
    <p>Система сборки и управления зависимостями</p>
</div>
<div class="tech-item">
    <h4>Allure</h4>
    <p><code>2.24.0</code></p>
    <p>Генерация отчетов</p>
</div>
<div class="tech-item">
    <h4>Chrome</h4>
    <p><code>142+</code></p>
    <p>Браузер для тестирования</p>
</div>

<br><h2>Установка и настройка</h2>

<h3>Предварительные требования</h3>
<ul>
    <li><code>Java 17</code> или выше</li>
    <li><code>Google Chrome</code> браузер</li>
    <li><code>Git</code></li>
</ul>

<h3>Шаг 1: Клонирование репозитория</h3>
<div class="code-block">
    <code>git clone &lt;repository-url&gt;</code><br>
    <code>cd &lt;project-directory&gt;</code>
</div>

<h3>Шаг 2: Конфигурация приложения</h3>
<p>Файл <code>application.conf</code> содержит настройки:</p>
<code>url = "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager"</code>

<h3>Шаг 3: Проверка окружения</h3>
<div class="code-block">
    <code>mvn -v</code><br>
    <code>java -version</code>
</div>

<br><h2>Запуск тестов</h2>

<h3>Все тесты</h3>
<div class="command"><code>mvn test</code></div>

<h3>С детализированным логированием</h3>
<div class="command"><code>mvn test -X</code></div>

<h3>Без кэширования</h3>
<div class="command"><code>mvn clean test</code></div>

<h3>Конкретный тест</h3>
<div class="command"><code>mvn -Dtest=ActionCustomerTest#testAddDefaultCustomer test</code></div>

<h3>Тест-кейсы</h3>
<div class="test-case success">
    <h4><code>testAddDefaultCustomer</code></h4>
    <p>Стандартное добавление клиента</p>
    <p><strong>Данные:</strong> Латинские символы</p>
</div>
<div class="test-case success">
    <h4><code>testAddCustLowerName</code></h4>
    <p>Фамилия в нижнем регистре</p>
    <p><strong>Данные:</strong> <code>"petrov"</code></p>
</div>
<div class="test-case success">
    <h4><code>testSortByName</code></h4>
    <p>Сортировка по имени</p>
    <p><strong>Ожидание:</strong> A-Z / Z-A порядок</p>
</div>
<div class="test-case warning">
    <h4><code>testDelCustomer</code></h4>
    <p>Удаление по средней длине имени</p>
    <p><strong>Логика:</strong> Алгоритмический выбор</p>
</div>

<br><h2>Генерация отчетов</h2>

<h3>Allure отчеты</h3>
<p>Генерация HTML отчета</p>
<code>mvn allure:report</code>
<p># Запуск веб-сервера</p>
<code>mvn allure:serve</code>

<br><h2>CI/CD</h2>

<h3>Поддерживаемые системы</h3>
<ul>
    <li>✅ <code>GitHub Actions</code></li>
    <li>✅ <code>GitLab CI</code></li>
    <li>✅ <code>Jenkins</code></li>
</ul>

<h3>Что делает CI/CD:</h3>
<ul>
    <li>Автоматический запуск тестов при <code>push</code> и <code>PR</code></li>
    <li>Установка <code>Chrome</code> в CI-окружении</li>
    <li>Кэширование зависимостей <code>Maven</code></li>
    <li>Генерация <code>Allure</code> отчетов</li>
    <li>Сохранение артефактов тестов</li>
    <li>Уведомления о результатах</li>
</ul>

<h3>Браузерные требования</h3>
<ul>
    <li><strong>Chrome:</strong> версия <code>141+</code></li>
    <li><strong>Разрешение:</strong> <code>1920x1080</code> рекомендуется</li>
    <li><strong>Права:</strong> возможность установки расширений</li>
</ul>
