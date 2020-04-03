CREATE TABLE refuel_transactions(
refuel_id	INT IDENTITY(1,1)	NOT NULL
,doc_no	NVARCHAR(100)	NULL
,doc_date	DATE	NULL
,vehicle_id	INT	NULL
,driver_id	INT	NULL
,pao_id	INT	NULL
,odo_reading	INT	NULL
,gas_station_id	INT	NULL
,no_liters	DECIMAL(20)	NULL
,unit_price	DECIMAL(20)	NULL
,refuel_amount	DECIMAL(20)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL
,is_posted	CHAR(1)	NULL
,posted_date	DATETIME	NULL)