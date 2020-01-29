CREATE TABLE transactions(
transaction_id	INT IDENTITY(1,1)	NOT NULL
,transaction_date	DATETIME	NULL
,vehicle_id	INT	NULL
,route_id	INT	NULL
,from_id	INT	NULL
,to_id	INT	NULL
,no_regular	INT	NULL
,no_students	INT	NULL
,no_sc	INT	NULL
,no_pwd	INT	NULL
,paid_amount	DECIMAL(20)	NULL
,customer_id	INT	NULL
,payment_type	CHAR(10)	NULL
,payment_code	NTEXT(2147483646)	NULL
,qr_id	INT	NULL
,created_by	INT	NULL)