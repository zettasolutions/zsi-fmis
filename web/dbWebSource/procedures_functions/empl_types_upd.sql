
CREATE PROCEDURE [dbo].[empl_types_upd]
(
    @tt    empl_types_tt READONLY
   ,@user_id int
)
AS

BEGIN
-- Update Process
    UPDATE a 
        SET  empl_type_code			= b.empl_type_code
			,empl_type_desc			= b.empl_type_desc
			
     FROM dbo.empl_types a INNER JOIN @tt b
        ON a.empl_type_code = b.empl_type_code 
       WHERE (
				isnull(a.empl_type_code,'') <> isnull(b.empl_type_code,'')   
			OR  isnull(a.empl_type_desc,'') <> isnull(b.empl_type_desc,'')   
		
	   )

-- Insert Process

    INSERT INTO empl_types (
         empl_type_code
		,empl_type_desc
		
        )
    SELECT 
		 empl_type_code
		,empl_type_desc
	   
    FROM @tt
    WHERE empl_type_code is not null
	and empl_type_desc is not null  
END

