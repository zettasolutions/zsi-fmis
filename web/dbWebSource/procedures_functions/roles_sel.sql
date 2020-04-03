CREATE PROCEDURE [dbo].[roles_sel]
(
   @user_id  int = null
   ,@role_id  INT = null
   ,@col_no	        int = 1       
   ,@order_no       int = 0   

)
AS
BEGIN
    SET NOCOUNT ON
	DECLARE @stmt		VARCHAR(4000);

	SET @stmt = 'SELECT * FROM roles_v WHERE 1=1 ' 
   
    IF ISNULL(@role_id,0)<> 0  
       SET @stmt = @stmt + ' AND role_id = ' + CAST(@role_id AS VARCHAR(20)); 

    
	SET @stmt= @stmt + ' ORDER BY ' + CAST(@col_no + 1 AS VARCHAR(1)) + ' ' + IIF(@order_no=1,'ASC','DESC');

	--IF @order_no = 0
	--   SET @stmt = @stmt + ' ASC'
    --ELSE
	--   SET @stmt = @stmt + ' DESC' 
	EXEC(@stmt);

END





