CREATE TYPE vehicle_repairs_tt AS TABLE(
repair_id	INT	NULL
,is_edited	CHAR(1)	NULL
,repair_date	DATE	NULL
,pms_type_id	INT	NULL
,vehicle_id	INT	NULL
,odo_reading	INT	NULL
,repair_amount	DECIMAL(12)	NULL
,repair_location	NVARCHAR(100)	NULL
,comment	NVARCHAR(0)	NULL
,status_id	INT	NULL)