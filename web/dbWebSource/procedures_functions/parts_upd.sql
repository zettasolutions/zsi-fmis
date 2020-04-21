CREATE PROCEDURE [dbo].[parts_upd]
(
    @tt    parts_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     part_type_id		= b.part_type_id
			,part_code			= b.part_code
			,part_desc			= b.part_desc
			,updated_by			= @user_id
			,updated_date		= GETDATE()
			

       FROM dbo.parts a INNER JOIN @tt b
	     ON a.part_id = b.part_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO parts(
         part_type_id
		,part_code
		,part_desc
		,created_by
		,created_date
		
    )
	SELECT 
		 part_type_id
		,part_code
		,part_desc
		,@user_id
		,GETDATE()
		
	FROM @tt 
	WHERE part_id IS NULL
	AND part_type_id IS NOT NULL
	AND part_code IS NOT NULL
	AND part_desc IS NOT NULL