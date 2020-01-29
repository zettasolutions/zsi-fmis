CREATE TYPE shifts_tt AS TABLE(
shift_id	INT	NULL
,is_edited	CHAR(1)	NULL
,shift_code	NVARCHAR(10)	NULL
,monday	CHAR(1)	NULL
,tuesday	CHAR(1)	NULL
,wednesday	CHAR(1)	NULL
,thursday	CHAR(1)	NULL
,friday	CHAR(1)	NULL
,saturday	CHAR(1)	NULL
,sunday	CHAR(1)	NULL
,no_hours	DECIMAL(20)	NULL
,next_day_out	CHAR(1)	NULL)