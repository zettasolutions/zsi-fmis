CREATE TABLE part_types(
part_type_id	INT IDENTITY(1,1)	NOT NULL
,part_type_code	NCHAR(20)	NULL
,part_type	NVARCHAR(100)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)