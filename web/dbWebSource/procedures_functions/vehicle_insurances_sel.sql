

CREATE PROCEDURE [dbo].[vehicle_insurances_sel]
(
    @user_id INT = NULL 
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.vehicle_insurances WHERE 1=1 '; 
	exec(@stmt);
 END;

