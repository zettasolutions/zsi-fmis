


CREATE PROCEDURE [dbo].[vehicle_daily_checklist_sel]
(
    @id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.vehicle_daily_checklist WHERE 1=1 ';

    
	IF @id <> '' 
	    SET @stmt = @stmt + ' AND id'+ @id;

	exec(@stmt);
 END;



