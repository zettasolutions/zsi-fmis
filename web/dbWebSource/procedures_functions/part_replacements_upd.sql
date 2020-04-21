
CREATE PROCEDURE [dbo].[part_replacements_upd]
(
    @tt    part_replacements_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	 		  replacement_date		= b.replacement_date	
			,vehicle_id				= b.vehicle_id
			,part_id				= b.part_id
			,part_qty				= b.part_qty
			,unit_id				= b.unit_id	
			,updated_by				= @user_id
			,updated_date			= GETDATE()
        FROM dbo.part_replacements a INNER JOIN @tt b
	     ON a.replacement_id = b.replacement_id 
		WHERE b.replacement_date IS NOT NULL
	    AND isnull(b.is_edited,'N')='Y'


-- Insert Process
	INSERT INTO part_replacements (
		 replacement_date
		,vehicle_id
		,part_id
		,part_qty
		,unit_id	
		,created_by
		,created_date
    )
	SELECT 
		  replacement_date
		,vehicle_id
		,part_id
		,part_qty
		,unit_id
		,@user_id
	    ,GETDATE()
	FROM @tt 
	WHERE replacement_id IS NULL
	AND replacement_date IS NOT NULL
	AND part_id IS NOT NULL
	AND part_qty IS NOT NULL
	AND vehicle_id IS NOT NULL;
 




