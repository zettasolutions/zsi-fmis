CREATE TYPE tax_table_tt AS TABLE(
id	INT	NULL
,is_edited	CHAR(1)	NULL
,pay_type_code	DECIMAL(20)	NULL
,cl_fr	DECIMAL(20)	NULL
,cl_to	DECIMAL(20)	NULL
,cl	DECIMAL(20)	NULL
,add_pct_cl	DECIMAL(20)	NULL)