CREATE PROCEDURE [dbo].[levels_sel] 
(
	@user_id INT
)
as
	

begin
set nocount on
   select * from levels order by level_no
end
