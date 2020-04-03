CREATE TYPE registration_info_tt AS TABLE(
registration_id	INT	NULL
,client_code	NVARCHAR(40)	NULL
,client_name	NVARCHAR(100)	NULL
,client_address	NVARCHAR(2000)	NULL
,contact_name	NVARCHAR(100)	NULL
,contact_no	NVARCHAR(100)	NULL
,tin	NVARCHAR(100)	NULL
,registered_date	DATE	NULL
,expiry_date	DATE	NULL)