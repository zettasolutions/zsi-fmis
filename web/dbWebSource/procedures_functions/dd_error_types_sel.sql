CREATE PROCEDURE [dbo].[dd_error_types_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT error_type_id, error_type FROM dbo.error_types WHERE 1=1; 
END