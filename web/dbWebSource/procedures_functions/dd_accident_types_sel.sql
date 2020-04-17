CREATE PROCEDURE [dbo].[dd_accident_types_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT accident_type_id, accident_type FROM dbo.accident_types WHERE 1=1; 
END