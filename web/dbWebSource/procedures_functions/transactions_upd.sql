
CREATE PROCEDURE [dbo].[transactions_upd]
(
    @tt    transactions_tt READONLY
   ,@user_id int
)
AS
-- Update Process
	UPDATE a 
		   SET 
	   	     transaction_date	= b. transaction_date	
			,vehicle_id			= b.vehicle_id
			,route_id			= b.route_id
			,from_id			= b.from_id
			,to_id				= b.to_id
			,no_regular			= b.no_regular
			,no_students		= b.no_students
			,no_sc				= b.no_sc
			,no_pwd				= b.no_pwd
			,paid_amount		= b.paid_amount
			,customer_id		= b.customer_id
			,payment_type		= b.payment_type
			,payment_code		= b.payment_code
			,qr_id				= b.qr_id
	   	    ,updated_by			= @user_id
			,updated_date		= GETDATE()

       FROM dbo.transactions a INNER JOIN @tt b
	     ON a.transaction_id = b.transaction_id
	     WHERE (
			isnull(b.is_edited,'')  <> ''
		);
-- Insert Process
	INSERT INTO transactions(
         transaction_date	
		,vehicle_id			
		,route_id			
		,from_id			
		,to_id				
		,no_regular			
		,no_students		
		,no_sc				
		,no_pwd				
		,paid_amount		
		,customer_id		
		,payment_type		
		,payment_code		
		,qr_id				
		,created_by
		,created_date
    )
	SELECT 
         transaction_date	
		,vehicle_id			
		,route_id			
		,from_id			
		,to_id				
		,no_regular			
		,no_students		
		,no_sc				
		,no_pwd				
		,paid_amount		
		,customer_id		
		,payment_type		
		,payment_code		
		,qr_id				
	   ,@user_id
	   , GETDATE()
	FROM @tt 
	WHERE transaction_id IS NULL
		and  paid_amount IS not NULL 
 






