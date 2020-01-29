

CREATE PROCEDURE [dbo].[asset_type_upd]
(
    @tt    asset_type_tt READONLY
   ,@user_id int
)
AS

BEGIN
-- Update Process
    UPDATE a 
        SET  asset_code		= b.asset_code
			,asset_type		= b.asset_type
			
     FROM dbo.asset_type a INNER JOIN @tt b
        ON a.id = b.id 
       WHERE (
				isnull(a.asset_code,'') <> isnull(b.asset_code,'')   
			OR  isnull(a.asset_type,'') <> isnull(b.asset_type,'')   
		
	   )

-- Insert Process

    INSERT INTO asset_type (
         asset_code
		,asset_type
		
        )
    SELECT 
        asset_code
	   ,asset_type
	   
    FROM @tt
    WHERE id IS NULL
END

