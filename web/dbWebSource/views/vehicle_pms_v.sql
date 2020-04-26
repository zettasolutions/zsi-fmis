CREATE VIEW dbo.vehicle_pms_v
AS
SELECT        dbo.vehicle_pms.pms_id, dbo.vehicle_pms.pms_date, dbo.vehicle_pms.vehicle_id, dbo.vehicle_pms.odo_reading, dbo.vehicle_pms.pm_amount, dbo.vehicle_pms.pm_location, dbo.vehicle_pms.comment, 
                         dbo.vehicle_pms.status_id, dbo.vehicle_pms.created_by, dbo.vehicle_pms.created_date, dbo.vehicle_pms.updated_by, dbo.vehicle_pms.updated_date, dbo.pms_types_v.pms_desc, dbo.vehicle_pms.pms_type_id
FROM            dbo.vehicle_pms INNER JOIN
                         dbo.pms_types_v ON dbo.vehicle_pms.pms_type_id = dbo.pms_types_v.pms_type_id
