CREATE PROCEDURE [dbo].[dd_pms_type_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT * FROM dbo.pms_types_v WHERE 1=1; 
END