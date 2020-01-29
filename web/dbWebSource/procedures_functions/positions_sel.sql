CREATE PROCEDURE [dbo].[positions_sel] 
(
	@user_id INT
)
as
	

begin
set nocount on
   select * from positions order by position_id
end
