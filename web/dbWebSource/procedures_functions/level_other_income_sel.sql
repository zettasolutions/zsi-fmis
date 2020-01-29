CREATE PROCEDURE [dbo].[level_other_income_sel] 
(
	@user_id INT
)
as
	

begin
set nocount on
   select * from level_other_income order by other_income_id
end
