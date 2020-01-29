
CREATE PROCEDURE [dbo].[employees_sel]
(
    @id					INT = null
   ,@employee_id		VARCHAR(100) = NULL
   ,@user_id			INT = NULL			
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
	SET @stmt = 'SELECT * FROM dbo.employees WHERE 1=1 ';

	IF @id <> '' 
	SET @stmt = @stmt + ' AND id' + CAST(@id AS VARCHAR);

	IF @employee_id <> ''
		SET @stmt = @stmt + ' AND employee_id'+ CAST(@id AS VARCHAR);
    
	set @stmt = @stmt + ' order by employee_id'
	exec(@stmt);
 END;


 


