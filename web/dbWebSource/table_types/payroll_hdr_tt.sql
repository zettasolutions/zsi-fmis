CREATE TYPE payroll_hdr_tt AS TABLE(
pay_period_id	INT	NULL
,is_edited	CHAR(1)	NULL
,period_date_from	DATE	NULL
,period_date_to	DATE	NULL
,pay_type_id	INT	NULL
,status_id	INT	NULL)