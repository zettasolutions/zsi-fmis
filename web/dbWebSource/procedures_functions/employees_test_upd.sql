CREATE PROCEDURE [dbo].[employees_test_upd]
(
    @tt    employees_test_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	      employee_id				= b.employee_id			
			 ,last_name					= b.last_name				
			 ,first_name				= b.first_name			
			 ,middle_name				= b.middle_name			
	 		 ,name_suffix				= b.name_suffix			
			 ,gender					= b.gender				
	   	     ,civil_status_code			= b.civil_status_code
			 ,empl_type_code			= b.empl_type_code
			 ,basic_pay					= b.basic_pay
			 ,pay_type_code				= b.pay_type_code
			 ,sss_no					= b.sss_no
			 ,tin						= b.tin
			 ,philhealth_no				= b.philhealth_no
			 ,hmdf_no					= b.hmdf_no
			 ,account_no				= b.account_no
			 ,is_active					= b.is_active
			 ,inactive_type_code		= b.inactive_type_code
			 ,inactive_date				= b.inactive_date
			 ,updated_by   = @user_id
			 ,updated_date = GETDATE()
        FROM dbo.employees_test a INNER JOIN @tt b
	     ON a.id = b.id 
		WHERE b.employee_id IS NOT NULL
	    AND isnull(b.is_edited,'N')='Y'


-- Insert Process
	INSERT INTO employees_test (
		 employee_id		
		,last_name			
		,first_name		
		,middle_name		
		,name_suffix		
		,gender			
		,civil_status_code	
		,empl_type_code	
		,basic_pay			
		,pay_type_code		
		,sss_no			
		,tin				
		,philhealth_no		
		,hmdf_no			
		,account_no		
		,is_active			
		,inactive_type_code
		,inactive_date		
		,created_by
		,created_date
    )
	SELECT 
		 employee_id		
		,last_name			
		,first_name		
		,middle_name		
		,name_suffix		
		,gender			
		,civil_status_code	
		,empl_type_code	
		,basic_pay			
		,pay_type_code		
		,sss_no			
		,tin				
		,philhealth_no		
		,hmdf_no			
		,account_no		
		,is_active			
		,inactive_type_code
		,inactive_date		
		,@user_id
	    ,GETDATE()
	FROM @tt 
	WHERE id IS NULL
      AND employee_id IS NOT NULL;



