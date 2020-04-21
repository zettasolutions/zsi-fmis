CREATE TABLE parts(
part_id	INT IDENTITY(1,1)	NOT NULL
,part_type_id	INT	NULL
,part_code	CHAR(10)	NULL
,part_desc	NVARCHAR(100)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)