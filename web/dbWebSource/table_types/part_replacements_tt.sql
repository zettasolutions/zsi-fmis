CREATE TYPE part_replacements_tt AS TABLE(
replacement_id	INT	NULL
,is_edited	CHAR(1)	NULL
,pms_id	INT	NULL
,repair_id	INT	NULL
,seq_no	INT	NULL
,part_id	INT	NULL
,part_qty	DECIMAL(10)	NULL
,unit_id	INT	NULL
,unit_cost	DECIMAL(10)	NULL
,is_replacement	CHAR(1)	NULL
,is_bnew	CHAR(1)	NULL)