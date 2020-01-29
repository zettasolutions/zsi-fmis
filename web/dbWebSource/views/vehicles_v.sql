
CREATE VIEW [dbo].[vehicles_v]
AS
SELECT        dbo.assets.*, dbo.routes_ref.route_code, dbo.routes_ref.route_desc
FROM            dbo.assets INNER JOIN
                         dbo.routes_ref ON dbo.assets.route_id = dbo.routes_ref.route_id

