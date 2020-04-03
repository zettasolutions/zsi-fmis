

CREATE PROCEDURE [dbo].[accident_transactions_sel]
(
    @accident_id  INT = null
   ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.accident_transactions WHERE 1=1 ';

    
	IF @accident_id <> '' 
	    SET @stmt = @stmt + ' AND accident_id='+ @accident_id;

	exec(@stmt);
 END;


