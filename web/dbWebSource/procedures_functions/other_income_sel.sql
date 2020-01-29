


CREATE PROCEDURE [dbo].[other_income_sel]
(
    @other_income_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.other_income WHERE 1=1 ';

    
	IF @other_income_id <> '' 
	    SET @stmt = @stmt + ' AND other_income_id'+ @other_income_id;

	exec(@stmt);
 END;



