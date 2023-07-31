-- liquibase formatted sql

-- changeset liquibase:1 contextFilter:dev
insert into users(email, firstname, lastname, password, role)
values ('test@mail.com', 'maksim', 'petrov', '$2y$10$gL5cNkdWdsB.Q44E5FsCt.fKLBYpg5uVH3Q83aX/lUFsGiz53G1Om', 'USER');