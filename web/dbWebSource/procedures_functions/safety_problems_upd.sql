
CREATE procedure [dbo].[safety_problems_upd](
   @safety_report_id  int=null
  ,@safety_report_date    date=null
  ,@vehicle_id int=null
  ,@safety_id int=null
  ,@comments nvarchar(max)=null
  ,@reported_by int=null
  ,@is_active char(1)=null
  ,@closed_date date=null
  ,@user_id   int=null
)
as
BEGIN
   SET NOCOUNT ON
	 IF ISNULL(@safety_report_id,0)=0
		INSERT INTO dbo.safety_problems
		 (
		  safety_report_date
		 ,vehicle_id
		 ,safety_id
		 ,comments
		 ,reported_by
		 ,is_active
		 ,closed_date
		 ,created_by
		 ,created_date
		 ) VALUES
		 (
		  @safety_report_date
		 ,@vehicle_id
		 ,@safety_id
		 ,@comments
		 ,@reported_by
		 ,@is_active
		 ,@closed_date
		 ,@user_id
		 ,GETDATE()
		 ) 

	ELSE
	   UPDATE dbo.safety_problems SET
			    safety_report_date	= @safety_report_date
			   ,vehicle_id			= @vehicle_id
			   ,safety_id			= @safety_id
  			   ,comments			= @comments
			   ,reported_by			= @reported_by
			   ,is_active			= @is_active
			   ,closed_date			= @closed_date
			   ,updated_by			= @user_id
			   ,updated_date		= GETDATE();
END;

 






