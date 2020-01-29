
create PROCEDURE [dbo].[filed_leaves_sel]
(
    @leave_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.filed_leaves WHERE 1=1 ';

    
	IF @leave_id <> '' 
	    SET @stmt = @stmt + ' AND id'+ @leave_id;

	exec(@stmt);
 END;
