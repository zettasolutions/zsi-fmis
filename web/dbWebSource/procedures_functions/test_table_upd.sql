


CREATE PROCEDURE [dbo].[test_table_upd]
(
    @tt    test_table_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
			  name				= b.name			
			  ,age				= b.age				
			  ,salary			= b.salary
			   ,updated_by   = @user_id
			,updated_date = GETDATE()	  
       FROM dbo.test_table a INNER JOIN @tt b
        ON a.id = b.id 
       AND isnull(b.is_edited,'N')='Y'


-- Insert Process
	INSERT INTO test_table (
		name
		,age
		,salary
		,created_by
		,created_date
    )
	SELECT 
		name
		,age
		,salary 
		 ,@user_id
	    ,GETDATE()
	FROM @tt 
	WHERE id IS NULL
  
  

