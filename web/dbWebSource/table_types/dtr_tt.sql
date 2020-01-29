CREATE TYPE dtr_tt AS TABLE(
id	INT	NULL
,is_edited	CHAR(1)	NULL
,employee_id	INT	NULL
,dt_in	DATETIME	NULL
,dt_out	DATETIME	NULL
,shift_id	INT	NULL
,reg_hours	DECIMAL(20)	NULL
,nd_hours	DECIMAL(20)	NULL
,total_hours	DECIMAL(20)	NULL)