CREATE TYPE vehicles_tt AS TABLE(
vehicle_id	INT	NULL
,is_edited	VARCHAR(1)	NULL
,plate_no	NVARCHAR(100)	NULL
,conduction_no	NVARCHAR(100)	NULL
,chassis_no	NVARCHAR(100)	NULL
,engine_no	NVARCHAR(100)	NULL
,date_acquired	DATE	NULL
,exp_registration_date	DATE	NULL
,exp_insurance_date	DATE	NULL
,vehicle_maker_id	INT	NULL
,odometer_reading	INT	NULL
,is_active	CHAR(1)	NULL
,status_id	INT	NULL
,hash_key	NVARCHAR(0)	NULL
,client_id	INT	NULL)