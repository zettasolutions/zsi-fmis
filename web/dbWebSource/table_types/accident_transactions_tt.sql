CREATE TYPE accident_transactions_tt AS TABLE(
accident_id	INT	NULL
,is_edited	CHAR(1)	NULL
,accident_date	DATE	NULL
,vehicle_id	INT	NULL
,driver_id	INT	NULL
,pao_id	INT	NULL
,accident_type_id	INT	NULL
,accident_level	CHAR(50)	NULL
,error_type_id	INT	NULL
,comments	NVARCHAR(0)	NULL)