


CREATE PROCEDURE [dbo].[filed_overtime_upd]
(
    @tt    filed_overtime_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   		 ot_filed_date			= b.ot_filed_date	
			,ot_type_id				= b.ot_type_id
			,employee_id			= b.employee_id 
			,ot_date				= b.ot_date
			,filed_ot_hours			= b.filed_ot_hours
			,approved_hours			= b.approved_hours
			,approved_by			= b.approved_by
			,approved_date			= b.approved_date
			,ot_reason				= b.ot_reason
			,approver_comment		= b.approver_comment
			,created_by				= @user_id
			,created_date			= GETDATE() 	    

       FROM dbo.filed_overtime a INNER JOIN @tt b
	     ON a.ot_id = b.ot_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO filed_overtime(
          	 ot_filed_date			
			,ot_type_id				
			,employee_id			
			,ot_date				
			,filed_ot_hours			
			,approved_hours			
			,approved_by			
			,approved_date			
			,ot_reason				
			,approver_comment		
			,created_by				
			,created_date
			 	
	   	     		
    )
	SELECT 
             ot_filed_date			
			,ot_type_id				
			,employee_id			
			,ot_date				
			,filed_ot_hours			
			,approved_hours			
			,approved_by			
			,approved_date			
			,ot_reason				
			,approver_comment	 		 		
			,@user_id
			, GETDATE()
			 
	FROM @tt 
	WHERE ot_id IS NULL
	and ot_filed_date is not null
	and ot_type_id is not null
	and employee_id is not null
	and ot_date is not null
	and filed_ot_hours is not null
 




