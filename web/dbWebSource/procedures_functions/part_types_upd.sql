

CREATE PROCEDURE [dbo].[part_types_upd]
(
    @tt    part_types_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     part_type_code		= b.part_type_code
			,part_type			= b.part_type
			,updated_by			= @user_id
			,updated_date		= GETDATE()
			

       FROM dbo.part_types a INNER JOIN @tt b
	     ON a.part_type_id = b.part_type_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO part_types(
         part_type_code
		,part_type
		,created_by
		,created_date
		
    )
	SELECT 
		 part_type_code
		,part_type
		,@user_id
		,GETDATE()
		
	FROM @tt 
	WHERE part_type_id IS NULL
	AND part_type_code IS NOT NULL
	AND part_type IS NOT NULL
 






