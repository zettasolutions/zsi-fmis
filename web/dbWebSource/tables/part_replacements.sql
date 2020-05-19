CREATE TABLE part_replacements(
replacement_id	INT IDENTITY(1,1)	NOT NULL
,pms_id	INT	NULL
,repair_id	INT	NULL
,seq_no	INT	NULL
,part_id	INT	NULL
,part_qty	DECIMAL(10)	NULL
,unit_id	INT	NULL
,unit_cost	DECIMAL(10)	NULL
,is_replacement	CHAR(1)	NULL
,is_bnew	CHAR(1)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)