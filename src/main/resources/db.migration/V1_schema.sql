
create sequence if not exists account_seq start with 100 increment by 1;
create sequence if not exists transaction_seq start with 100 increment by 1;

create table if not exists accounts(
    id bigint primary key default nextval('account_seq'),
    account_number varchar(16) not null unique,
    owner_name     varchar(255) not null,
    account_status varchar(20) not null default 'ACTIVE',
    balance      numeric(19, 2) not null default 0.00,
    create_time    timestamp    not null default now(),
    update_time    timestamp    not null default now(),

    constraint chk_balance_non_negative check (balance >= 0),
    constraint chk_account_status check (account_status in ('ACTIVE','BLOCKED','CLOSED')
));

create table if not exists transactions(
    id  bigint primary key default nextval('transaction_seq'),
    amount  numeric(19, 2) not null,
    status varchar(20) not null,
    type  varchar(30) not null,
    balance_after  numeric(19,2),
    failure_reason varchar(500),
    create_time timestamp not null default now(),
    source_account_id bigint,
    target_account_id bigint,

    constraint chk_amount_positive check (amount >0),
    constraint chk_transaction_status check (status in ('SUCCESS', 'FAILED')),
    constraint chk_transaction_type check (type in ('TRANSFER_OUT', 'TRANSFER_IN')),
    constraint fk_source_account foreign key (source_account_id) references accounts(id) on delete set null,
    constraint fk_target_account foreign key (target_account_id) references accounts(id) on delete set null
);
