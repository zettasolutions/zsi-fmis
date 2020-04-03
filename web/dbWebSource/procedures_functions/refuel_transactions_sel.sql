
CREATE PROCEDURE [dbo].[refuel_transactions_sel]
(
    @user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.refuel_transactions WHERE 1=1 ';

	exec(@stmt);
 END;

