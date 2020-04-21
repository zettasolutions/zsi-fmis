

CREATE PROCEDURE [dbo].[part_replacements_sel]
(
    @user_id INT = NULL
   ,@vehicle_id  INT = null
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.part_replacements WHERE 1=1';

	IF  ISNULL(@vehicle_id,0) <> 0
	    SET @stmt = @stmt + ' AND vehicle_id ='+ cast(@vehicle_id as varchar(20));
	 
	exec(@stmt);
 END;
