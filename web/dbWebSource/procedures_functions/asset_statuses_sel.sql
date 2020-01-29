
create PROCEDURE [dbo].[asset_statuses_sel]
(
    @asset_status_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.asset_statuses WHERE 1=1 ';

    
	IF @asset_status_id <> '' 
	    SET @stmt = @stmt + ' AND id'+ @asset_status_id;

	exec(@stmt);
 END;
