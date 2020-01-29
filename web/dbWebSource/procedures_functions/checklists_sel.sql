
CREATE PROCEDURE [dbo].[checklists_sel]
(
    @checklist_id		INT = null
   ,@checklist_code		NVARCHAR(50) = null
   ,@user_id			INT = NULL			
)
AS

BEGIN
SET NOCOUNT ON
	DECLARE @stmt		VARCHAR(4000);
	SET @stmt = 'SELECT * FROM dbo.checklists WHERE 1=1 ';

	IF @checklist_id <> '' 
	SET @stmt = @stmt + ' AND checklist_id' + CAST(@checklist_id AS VARCHAR);

	IF @checklist_code <> '' 
	SET @stmt = @stmt + ' AND checklist_code' + @checklist_code + '''';

	set @stmt = @stmt + ' order by checklist_code'
	exec(@stmt);
 END;


 


