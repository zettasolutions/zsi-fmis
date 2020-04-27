CREATE TABLE vehicle_registrations(
vehicle_registration_id	INT IDENTITY(1,1)	NOT NULL
,vehicle_id	INT	NOT NULL
,registration_no	NVARCHAR(100)	NOT NULL
,registration_date	DATE	NOT NULL
,expiry_date	DATE	NOT NULL
,paid_amount	DECIMAL(12)	NOT NULL
,created_by	INT	NULL
,created_date	DATETIME	NULL
,updated_by	INT	NULL
,updated_date	DATETIME	NULL)