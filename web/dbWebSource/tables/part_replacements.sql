CREATE TABLE part_replacements(
replacement_id	INT IDENTITY(1,1)	NOT NULL
,replacement_date	DATETIME	NULL
,vehicle_id	INT	NULL
,part_id	INT	NULL
,part_qty	DECIMAL(10)	NULL
,unit_id	INT	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)