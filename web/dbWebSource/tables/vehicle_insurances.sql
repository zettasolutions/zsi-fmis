CREATE TABLE vehicle_insurances(
vehicle_insurance_id	INT	NOT NULL
,vehicle_id	INT	NOT NULL
,insurance_no	NVARCHAR(100)	NOT NULL
,insurance_date	DATE	NOT NULL
,insurance_company_id	INT	NOT NULL
,expiry_date	DATE	NOT NULL
,insurance_type_id	INT	NOT NULL
,insured_amount	DECIMAL(12)	NOT NULL
,paid_amount	DECIMAL(12)	NOT NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)