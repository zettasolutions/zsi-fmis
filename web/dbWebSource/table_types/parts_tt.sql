CREATE TYPE parts_tt AS TABLE(
part_id	INT	NULL
,is_edited	VARCHAR(1)	NULL
,part_type_id	INT	NULL
,part_code	CHAR(10)	NULL
,part_desc	NVARCHAR(100)	NULL)