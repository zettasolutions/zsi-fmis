


CREATE PROCEDURE [dbo].[dtr_sel]
(
    @id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.dtr WHERE 1=1 ';

    
	IF @id <> '' 
	    SET @stmt = @stmt + ' AND id'+ @id;

	exec(@stmt);
 END;



