



CREATE PROCEDURE [dbo].[shifts_sel]
(
    @shift_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.shifts WHERE 1=1 ';

    
	IF @shift_id <> '' 
	    SET @stmt = @stmt + ' AND shift_id'+ @shift_id;

	exec(@stmt);
 END;




