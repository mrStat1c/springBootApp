SET sql_safe_updates = 0;

DELETE FROM customer;

INSERT INTO customer (id, name, driver_lic, phone)
VALUES
(1, 'Martin JJJ', '99 11 111111', '+79061111111'),
(2, 'Mary A', '99 22 222222', '+79062222222'),
(3, 'Soler from Astora', '99 33 333333', '+79063333333');

DELETE FROM motocycle;

INSERT INTO motocycle (customer_id, model, vin, `release`, weight, power, type)
VALUES
(2, 'Bajaj Dominar 400', 'FM344LL544I', '2020', 194, 40, 'NAKED'),
(2, 'Yamaha MT-03', 'AG4KK09GS0', '2020', 165, 42, 'NAKED'),
(NULL, 'Honda Rebel 1100', 'JDFA345PGD1', '2022', 204, 75, 'CRUISER');