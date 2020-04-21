CREATE PROCEDURE [dbo].[dd_units_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT * FROM dbo.units WHERE 1=1; 
END
