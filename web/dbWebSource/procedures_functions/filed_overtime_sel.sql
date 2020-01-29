create PROCEDURE [dbo].[filed_overtime_sel]
(
    @ot_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.filed_overtime WHERE 1=1 ';

    
	IF @ot_id <> '' 
	    SET @stmt = @stmt + ' AND id'+ @ot_id;

	exec(@stmt);
 END;
