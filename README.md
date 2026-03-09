Run with Docker
docker-compose up --build
Service will be available on http://localhost:8080.

API

Create transfer
POST/api/transfers

body example:
{
"sourceAccountNumber": "string",
"targetAccountNumber": "string",
"amount": 0.01
}

Responses:
{
"transactionId": 0,
"sourceAccountNumber": "string",
"targetAccountNumber": "string",
"amount": 0,
"status": "SUCCESS",
"sourceBalanceAfter": 0,
"createdAt": "2026-03-09T10:28:54.579Z"
}

Account statement
GET /api/accounts/{accountNumber}/statement?from=2026-01-01&to=2026-01-31&page=0&size=20
Responses:
{
"pageNumber": 0,
"pageSize": 0,
"totalElements": 0,
"totalPages": 0,
"content": [{
            "transactionId": 0,
            "operationDate": "2026-03-09T10:25:45.644Z",
            "type": "TRANSFER_OUT",
            "amount": 0,
            "balanceAfter": 0,
            "status": "SUCCESS"
           }]
     }

SQL tasks
See queries.sql for required SQL queries and index optimization.