CREATE PROCEDURE [dbo].[dd_part_types_sel]
(
   @user_id  int = null
)
AS
BEGIN
      SELECT part_type_id, part_type FROM dbo.part_types WHERE 1=1; 
END