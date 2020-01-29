
CREATE PROCEDURE [dbo].[afcs_pao_sel]  
(  
   @hash_key NVARCHAR(MAX)
   , @user_id INT = NULL
)  
AS  
BEGIN  
	SET NOCOUNT ON;
	SELECT id pao_id,concat(last_name,', ',first_name,dbo.isNotNull(middle_name,concat(' ',substring(middle_name,1,1),'.'))) emp_lfm_name
	FROM lmdo_payroll.dbo.employees WHERE position_id=4 
	and is_active='Y'
	AND emp_hash_key = @hash_key
	
END;