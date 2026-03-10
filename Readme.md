Banking REST Service
REST API для банковских переводов между счетами и получения выписок по счетам.

Технологии
Java 21, Spring Boot 3.x
Gradle 8.14+
PostgreSQL
Docker & Docker Compose

Запуск
docker-compose up --build
Сервис будет доступен на http://localhost:8080

API Endpoints

1. Создать перевод
   POST /api/transfers
   Request:
   json{
   "sourceAccountNumber": "1000000000000001",
   "targetAccountNumber": "1000000000000002",
   "amount": 1000.00
   }
   Response:
   json{
   "transactionId": 12345,
   "sourceAccountNumber": "1000000000000001",
   "targetAccountNumber": "1000000000000002",
   "amount": 1000.50,
   "status": "SUCCESS",
   "sourceBalanceAfter": 3999.50,
   "createdAt": "2026-03-10T10:30:00.000Z"
   }

2. Получить выписку по счету
   GET /api/accounts/{accountNumber}/statement
   Параметры:

accountNumber (path) - номер счета
from (query) - дата начала (YYYY-MM-DD)
to (query) - дата окончания (YYYY-MM-DD)
pageNumber (query) - номер страницы (default: 1)
pageSize (query) - размер страницы (default: 16)

Response:
{
"pageNumber": 0,
"pageSize": 20,
"totalElements": 45,
"totalPages": 3,
"content": [
{
"transactionId": 12345,
"operationDate": "2026-01-15T14:30:00.000Z",
"type": "TRANSFER_OUT",
"amount": 500.00,
"balanceAfter": 4500.00,
"status": "SUCCESS"
}
]
}

SQL запросы
См. файл queries.sql для необходимых SQL запросов и оптимизации индексов.