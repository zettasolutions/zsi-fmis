CREATE PROCEDURE [dbo].[leave_types_sel] 
(
	@user_id INT
)
as
	

begin
set nocount on
   select * from leave_types order by leave_code
end