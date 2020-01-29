
CREATE PROCEDURE [dbo].[philhealth_table_sel]
(
    @philhealth_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.philhealth_table WHERE 1=1 ';

    
	IF @philhealth_id <> '' 
	    SET @stmt = @stmt + ' AND philhealth_id'+ @philhealth_id;

	exec(@stmt);
 END;

