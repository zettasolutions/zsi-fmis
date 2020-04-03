CREATE TABLE clients(
client_id	INT IDENTITY(1,1)	NOT NULL
,registration_key	NVARCHAR(100)	NULL
,client_code	NVARCHAR(40)	NULL
,client_name	NVARCHAR(100)	NULL
,client_address	NVARCHAR(2000)	NULL
,contact_name	NVARCHAR(100)	NULL
,contact_nos	NVARCHAR(100)	NULL
,client_tin	NVARCHAR(100)	NULL
,registered_date	DATE	NULL
,expiry_date	DATE	NULL
,account_mngr_id	INT	NULL
,status_id	INT	NULL
,is_active	INT	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)