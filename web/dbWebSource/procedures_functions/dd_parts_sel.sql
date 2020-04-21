CREATE PROCEDURE [dbo].[dd_parts_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT part_id, part_desc FROM dbo.parts WHERE 1=1; 
END
