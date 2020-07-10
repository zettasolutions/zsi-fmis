CREATE TABLE users(
user_id	INT IDENTITY(1,1)	NOT NULL
,logon	NVARCHAR(100)	NOT NULL
,last_name	NVARCHAR(200)	NOT NULL
,first_name	NVARCHAR(200)	NOT NULL
,middle_name	NVARCHAR(2)	NULL
,full_name	NVARCHAR(404)	NOT NULL
,role_id	INT	NULL
,password	NVARCHAR(100)	NULL
,name_suffix	NVARCHAR(100)	NULL
,client_id	INT	NULL
,is_admin	VARCHAR(1)	NULL
,is_active	VARCHAR(1)	NULL
,img_filename	VARCHAR(200)	NULL
,is_developer	VARCHAR(1)	NULL
,created_by	INT	NULL
,created_date	DATETIMEOFFSET	NULL
,updated_by	INT	NULL
,updated_date	DATETIMEOFFSET	NULL
,hash_key	NTEXT(2147483646)	NULL)