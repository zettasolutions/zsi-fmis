
CREATE PROCEDURE [dbo].[afcs_assets_routes_ref_sel]  
(  
   @hash_key NVARCHAR(MAX)
   , @user_id INT = NULL
)  
AS  
BEGIN  
	SET NOCOUNT ON;

	SELECT
		b.asset_code
		, a.[asset_no]
		, a.is_active
		, c.route_code
		, c.route_desc
		, d.route_no
		, d.[location]
		, d.distance_km
		, d.seq_no
	FROM dbo.assets a 
	JOIN dbo.asset_types b
	ON a.asset_type_id = b.id
	JOIN dbo.routes_ref c
	ON a.route_id = c.route_id
	JOIN dbo.route_details d
	ON c.route_id = d.route_id
	WHERE 1 = 1
	AND hash_key = @hash_key
	ORDER BY
		d.route_no
		, d.seq_no
END;