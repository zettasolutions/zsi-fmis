CREATE VIEW dbo.vehicle_repairs_v
AS
SELECT        dbo.vehicle_repairs.repair_id, dbo.vehicle_repairs.repair_date, dbo.vehicle_repairs.vehicle_id, dbo.vehicle_repairs.odo_reading, dbo.vehicle_repairs.repair_amount, dbo.vehicle_repairs.repair_location, 
                         dbo.vehicle_repairs.comment, dbo.vehicle_repairs.status_id, dbo.vehicle_repairs.created_by, dbo.vehicle_repairs.created_date, dbo.vehicle_repairs.updated_by, dbo.vehicle_repairs.updated_date, 
                         dbo.pms_types_v.pms_desc, dbo.vehicle_repairs.pms_type_id
FROM            dbo.vehicle_repairs INNER JOIN
                         dbo.pms_types_v ON dbo.vehicle_repairs.pms_type_id = dbo.pms_types_v.pms_type_id
