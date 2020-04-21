CREATE TYPE part_replacements_tt AS TABLE(
replacement_id	INT	NULL
,is_edited	CHAR(1)	NULL
,replacement_date	DATETIME	NULL
,vehicle_id	INT	NULL
,part_id	INT	NULL
,part_qty	DECIMAL(10)	NULL
,unit_id	INT	NULL)