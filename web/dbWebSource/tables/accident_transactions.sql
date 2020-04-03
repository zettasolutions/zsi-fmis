CREATE TABLE accident_transactions(
accident_id	INT IDENTITY(1,1)	NOT NULL
,accident_date	DATE	NULL
,vehicle_id	INT	NULL
,driver_id	INT	NULL
,pao_id	INT	NULL
,accident_type_id	INT	NULL
,accident_level	CHAR(50)	NULL
,error_type_id	INT	NULL
,comments	NTEXT(2147483646)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)