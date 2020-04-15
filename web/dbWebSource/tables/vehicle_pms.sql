CREATE TABLE vehicle_pms(
pms_id	INT IDENTITY(1,1)	NOT NULL
,pms_date	DATE	NULL
,pms_type_id	INT	NULL
,vehicle_id	INT	NULL
,odo_reading	INT	NULL
,pm_amount	DECIMAL(12)	NULL
,pm_location	NVARCHAR(100)	NULL
,comment	NTEXT(2147483646)	NULL
,status_id	INT	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)