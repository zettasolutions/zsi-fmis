CREATE TYPE refuel_transactions_tt AS TABLE(
refuel_id	INT	NULL
,is_edited	CHAR(1)	NULL
,doc_date	DATE	NULL
,doc_no	NVARCHAR(100)	NULL
,vehicle_id	INT	NULL
,driver_id	INT	NULL
,pao_id	INT	NULL
,odo_reading	INT	NULL
,gas_station_id	INT	NULL
,no_liters	DECIMAL(20)	NULL
,unit_price	DECIMAL(20)	NULL
,refuel_amount	DECIMAL(20)	NULL)