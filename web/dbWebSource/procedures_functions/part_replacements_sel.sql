

CREATE PROCEDURE [dbo].[part_replacements_sel]
(
    @pms_id  INT = null
   ,@repair_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
SET NOCOUNT ON
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.part_replacements_v ';

	IF ISNULL(@pms_id,0)<>0
	   SET @stmt = @stmt + ' WHERE pms_id = ' + CAST(@pms_id AS VARCHAR(20))
    
	IF ISNULL(@repair_id,0)<>0
	   SET @stmt = @stmt + ' WHERE repair_id = ' + CAST(@repair_id AS VARCHAR(20))
	
	SET @stmt = @stmt +  ' ORDER BY seq_no'

	exec(@stmt);
 END;
