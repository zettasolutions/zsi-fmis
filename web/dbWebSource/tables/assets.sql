CREATE TABLE assets(
asset_id	INT IDENTITY(1,1)	NOT NULL
,asset_type_id	INT	NULL
,asset_no	NVARCHAR(100)	NULL
,date_acquired	DATE	NULL
,exp_registration_date	DATE	NULL
,exp_insurance_date	DATE	NULL
,status_id	INT	NULL
,is_active	CHAR(1)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL
,route_id	INT	NULL
,hash_key	NTEXT(2147483646)	NULL)