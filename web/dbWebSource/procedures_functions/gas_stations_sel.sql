

CREATE PROCEDURE [dbo].[gas_stations_sel]
(
     @gas_station_id  INT = null
	,@is_active CHAR(1)='Y'
    ,@user_id INT = NULL
)
AS
BEGIN
	DECLARE @stmt		VARCHAR(4000);
 	SET @stmt = 'SELECT * FROM dbo.gas_stations WHERE 1=1 ';

    
	IF @gas_station_id <> '' 
	    SET @stmt = @stmt + ' AND gas_station_id='+ @gas_station_id;
	IF isnull(@is_active,'') <> ''
		SET @stmt = @stmt + ' AND is_active='''+ @is_active + '''';
	exec(@stmt);
 END;


