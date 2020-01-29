
CREATE PROCEDURE [dbo].[pay_types_upd]
(
    @tt    pay_types_tt READONLY
   ,@user_id int
)
AS

BEGIN
-- Update Process
    UPDATE a 
        SET  pay_type_code		= b.pay_type_code
			,pay_type_desc		= b.pay_type_desc
			
     FROM dbo.pay_types a INNER JOIN @tt b
        ON a.pay_type_code = b.pay_type_code 
       WHERE (
				isnull(a.pay_type_code,'') <> isnull(b.pay_type_code,'')   
			OR  isnull(a.pay_type_desc,'') <> isnull(b.pay_type_desc,'')   
		
	   )

-- Insert Process

    INSERT INTO pay_types (
         pay_type_code
		,pay_type_desc
		
        )
    SELECT 
         pay_type_code
		,pay_type_desc
	   
    FROM @tt
    WHERE pay_type_code is not null
	and pay_type_desc is not null  
END

