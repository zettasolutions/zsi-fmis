CREATE TABLE vehicle_insurances(
vehicle_insurance_id	INT IDENTITY(1,1)	NOT NULL
,vehicle_id	INT	NULL
,insurance_no	NVARCHAR(100)	NULL
,insurance_date	DATE	NULL
,insurance_company_id	INT	NULL
,expiry_date	DATE	NULL
,insurance_type_id	INT	NULL
,insured_amount	DECIMAL(12)	NULL
,paid_amount	DECIMAL(12)	NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)