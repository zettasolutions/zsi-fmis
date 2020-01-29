
CREATE PROCEDURE [dbo].[filed_leaves_upd]
(
    @tt    filed_leaves_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   		 employee_id			= b.employee_id	
			,leave_type_id			= b.leave_type_id
			,filed_date				= b.filed_date 
			,leave_date				= b.leave_date
			,filed_hours			= b.filed_hours
			,approved_hours			= b.approved_hours
			,is_approved			= b.is_approved
			,is_approved_by			= b.is_approved_by
			,is_approved_date		= b.is_approved_date
			,leave_reason			= b.leave_reason
			,approver_comment		= b.approver_comment 
			,created_by				= @user_id
			,created_date			= GETDATE() 	    

       FROM dbo.filed_leaves a INNER JOIN @tt b
	     ON a.leave_id = b.leave_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO filed_leaves(
          	  employee_id			
			,leave_type_id				
			,filed_date			
			,leave_date				
			,filed_hours			
			,approved_hours			
			,is_approved			
			,is_approved_by			
			,is_approved_date				
			,leave_reason
			,approver_comment		
			,created_by				
			,created_date
			 	
	   	     		
    )
	SELECT 
             employee_id			
			,leave_type_id				
			,filed_date			
			,leave_date				
			,filed_hours			
			,approved_hours			
			,is_approved			
			,is_approved_by			
			,is_approved_date				
			,leave_reason
			,approver_comment	 		 		
			,@user_id
			, GETDATE()
			 
	FROM @tt 
	WHERE leave_id IS NULL
	and employee_id is not null
	and leave_type_id is not null 
	 
 




