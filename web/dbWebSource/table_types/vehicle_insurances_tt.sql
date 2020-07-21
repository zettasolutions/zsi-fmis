CREATE TYPE vehicle_insurances_tt AS TABLE(
vehicle_insurance_id	INT	NULL
,is_edited	VARCHAR(1)	NULL
,vehicle_id	INT	NULL
,insurance_no	NVARCHAR(100)	NULL
,insurance_date	DATE	NULL
,insurance_company_id	INT	NULL
,expiry_date	DATE	NULL
,insurance_type_id	INT	NULL
,insured_amount	DECIMAL(12)	NULL
,paid_amount	DECIMAL(12)	NULL)