CREATE TYPE assets_tt AS TABLE(
asset_id	INT	NULL
,is_edited	CHAR(1)	NULL
,asset_type_id	INT	NULL
,asset_no	NVARCHAR(100)	NULL
,date_acquired	DATE	NULL
,exp_registration_date	DATE	NULL
,exp_insurance_date	DATE	NULL
,status_id	INT	NULL
,is_active	CHAR(1)	NULL)