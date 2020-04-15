CREATE TABLE safety_list(
safety_id	INT IDENTITY(1,1)	NOT NULL
,safety_name	NVARCHAR(100)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)