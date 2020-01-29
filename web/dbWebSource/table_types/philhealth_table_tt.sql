CREATE TYPE philhealth_table_tt AS TABLE(
philhealth_id	INT	NULL
,is_edited	CHAR(1)	NULL
,salary_fr	DECIMAL(20)	NULL
,salary_to	DECIMAL(20)	NULL
,salary_base	DECIMAL(20)	NULL
,total_monthly_premium	DECIMAL(20)	NULL
,ee_share	DECIMAL(20)	NULL
,er_share	DECIMAL(20)	NULL)