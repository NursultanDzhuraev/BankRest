INSERT INTO accounts (account_number, owner_name, account_status, balance)
VALUES
    ('1000000000000001', 'Nursultan Juraev', 'ACTIVE', 5000.00),
    ('1000000000000002', 'Aida Bekova', 'ACTIVE', 12000.50),
    ('1000000000000003', 'Azamat Turgunov', 'BLOCKED', 300.00),
    ('1000000000000004', 'Elvira Sadykova', 'ACTIVE', 750.75),
    ('1000000000000005', 'Bakyt Omuraliev', 'CLOSED', 0.00);



-- ТОП-5 счетов по количеству транзакций за последний месяц
select
    a.id,
    a.account_number,
    a.owner_name,
    count(t.id) as transaction_cont
from  accounts a
left join transactions t
on (a.id = t.source_account_id or a.id = t.target_account_id)
and t.status = 'SUCCESS'
and t.create_time >= current_timestamp -interval '30 days'
group by a.id, a.account_number, a.owner_name
order by transaction_cont desc limit 5;

-- Общую сумму переводов за выбранный период

select
     sum(t.amount) as total_transfer_amount
from transactions t
where t.type in ('TRANSFER_OUT', 'TRANSFER_IN')
and t.status = 'SUCCESS'
and t.create_time between '2026-02-01' and '2026-03-12';

-- Найти счета с отрицательным балансом

select
    id,
    account_number,
    owner_name,
    balance
from accounts where balance < 0;

-- Оптимизации
create index idx_transactions_create_time on transactions(create_time);

select
    sum(t.amount) as total_transfer_amount
from transactions t
where t.type in ('TRANSFER_OUT', 'TRANSFER_IN')
and t.create_time between '2026-02-01' and '2026-03-11';

-- Без индекса:
-- Проверяет каждую строку таблицы transactions
--С индексом:
--быстро находит диапазон дат и читает только нужные записи