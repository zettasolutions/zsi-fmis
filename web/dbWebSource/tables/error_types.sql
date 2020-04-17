CREATE TABLE error_types(
error_type_id	INT IDENTITY(1,1)	NOT NULL
,error_type	NVARCHAR(100)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)