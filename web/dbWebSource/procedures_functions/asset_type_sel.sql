



CREATE PROCEDURE [dbo].[asset_type_sel]
(
    @id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.asset_type WHERE 1=1 ';

    
	IF @id <> '' 
	    SET @stmt = @stmt + ' AND id'+ @id;

	exec(@stmt);
 END;




