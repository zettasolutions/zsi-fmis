

CREATE PROCEDURE [dbo].[transactions_sel]
(
    @transaction_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.transactions WHERE 1=1 ';

    
	IF @transaction_id <> '' 
	    SET @stmt = @stmt + ' AND transaction_id='+ @transaction_id;

	exec(@stmt);
 END;


