CREATE PROCEDURE [dbo].[dd_safety_list_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT safety_id, safety_name FROM dbo.safety_list WHERE 1=1; 
END





