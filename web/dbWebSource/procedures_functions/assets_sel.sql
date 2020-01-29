CREATE PROCEDURE [dbo].[assets_sel]
(
    @asset_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.assets WHERE 1=1 ';

    
	IF @asset_id <> '' 
	    SET @stmt = @stmt + ' AND id'+ @asset_id;

	exec(@stmt);
 END;
