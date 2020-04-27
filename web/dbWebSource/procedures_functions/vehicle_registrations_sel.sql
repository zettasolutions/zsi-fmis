
CREATE PROCEDURE [dbo].[vehicle_registrations_sel]
(
   @user_id INT = NULL
  ,@vehicle_registration_id  INT = null 
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.vehicle_registrations WHERE 1=1 ';

    IF  ISNULL(@vehicle_registration_id,0) <> 0
	    SET @stmt = @stmt + ' AND vehicle_registration_id ='+ cast(@vehicle_registration_id as varchar(20));
 

	exec(@stmt);
 END;
