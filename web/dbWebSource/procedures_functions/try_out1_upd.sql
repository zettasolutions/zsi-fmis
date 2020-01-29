


CREATE PROCEDURE [dbo].[try_out1_upd]
(
    @tt    try_out1_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
			  name				= b.name			
			  ,email				= b.email				
			  ,password			= b.password
			   ,updated_by   = @user_id
			,updated_date = GETDATE()	  
       FROM dbo.try_out1 a INNER JOIN @tt b
        ON a.id = b.id 
       AND isnull(b.is_edited,'N')='Y'


-- Insert Process
	INSERT INTO try_out1 (
		name
		,email
		,password
		,created_by
		,created_date
    )
	SELECT 
		name
		,email
		,password 
		 ,@user_id
	    ,GETDATE()
	FROM @tt 
	WHERE id IS NULL
  
  

