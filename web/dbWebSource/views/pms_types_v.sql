
CREATE VIEW [dbo].[pms_types_v]
AS
SELECT pms_type_id, pms_odo, CONCAT(pms_odo, ' Kms.')AS pms_desc
FROM     dbo.pms_types
