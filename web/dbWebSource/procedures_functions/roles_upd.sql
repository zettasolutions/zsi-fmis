

CREATE PROCEDURE [dbo].[roles_upd]
(
   @tt    roles_tt READONLY
   ,@user_id int
)
AS
SET NOCOUNT ON
DECLARE @updated_count INT;
-- Update Process
	UPDATE a 
		 SET role_name         = b.role_name
		   	 ,updated_by        = @user_id
			,updated_date      = GETDATE()
       FROM dbo.roles a INNER JOIN @tt b
	     ON a.role_id = b.role_id 
		WHERE b.role_name IS NOT NULL
	    AND isnull(b.is_edited,'N')='Y'


SET @updated_count = @@ROWCOUNT;

-- Insert Process
	INSERT INTO roles (
		 role_name
		,created_by
		,created_date
    )
	SELECT 
		 role_name
	    ,@user_id
	    ,GETDATE()
	FROM @tt 
	WHERE role_id IS NULL 
	  AND role_name IS NOT NULL;

SET @updated_count = @updated_count + @@ROWCOUNT;
RETURN @updated_count;







