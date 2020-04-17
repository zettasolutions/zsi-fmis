CREATE TABLE accident_types(
accident_type_id	INT IDENTITY(1,1)	NOT NULL
,accident_type	NVARCHAR(100)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)