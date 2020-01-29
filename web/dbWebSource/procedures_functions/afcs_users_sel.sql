
CREATE PROCEDURE [dbo].[afcs_users_sel]  
(  
   @hash_key NVARCHAR(MAX)
   , @user_id INT = NULL
)  
AS  
BEGIN  
	SET NOCOUNT ON;

	SELECT
		[id]
		, [last_name] AS [logon]
		, [last_name]
		, [first_name]
		, [middle_name]
		, [is_active]
		, CONCAT([first_name], ' ', [middle_name], ' ', [last_name]) AS full_name
	FROM [zsi_payroll].dbo.employees WHERE 1 = 1
	AND emp_hash_key = @hash_key
	
END;