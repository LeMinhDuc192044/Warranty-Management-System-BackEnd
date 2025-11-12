-- Cập nhật tên các xe hiện có (sử dụng cột Model_Name)
UPDATE Electric_Vehicle_Type SET Model_Name = 'VF 3' WHERE ID_Electric_Vehicle_Type = 'EVT001';
UPDATE Electric_Vehicle_Type SET Model_Name = 'VF 5' WHERE ID_Electric_Vehicle_Type = 'EVT002';
UPDATE Electric_Vehicle_Type SET Model_Name = 'VF 6' WHERE ID_Electric_Vehicle_Type = 'EVT003';
UPDATE Electric_Vehicle_Type SET Model_Name = 'VF 7' WHERE ID_Electric_Vehicle_Type = 'EVT004';
UPDATE Electric_Vehicle_Type SET Model_Name = 'VF 8' WHERE ID_Electric_Vehicle_Type = 'EVT005';
UPDATE Electric_Vehicle_Type SET Model_Name = 'VF 9' WHERE ID_Electric_Vehicle_Type = 'EVT006';

-- Thêm VF e34 nếu chưa có
INSERT INTO Electric_Vehicle_Type (ID_Electric_Vehicle_Type, Model_Name, description)
VALUES ('EVT007', 'VF e34', 'VinFast e34 - Xe điện phổ thông')
ON DUPLICATE KEY UPDATE Model_Name = 'VF e34';

-- Thêm các loại xe mới
INSERT INTO Electric_Vehicle_Type (ID_Electric_Vehicle_Type, Model_Name, description)
VALUES 
    ('EVT008', 'Limo Green', 'Xe điện thương mại - Limo Green'),
    ('EVT009', 'Minio Green', 'Xe điện mini - Minio Green'),
    ('EVT010', 'Herio Green', 'Xe điện Hero - Herio Green'),
    ('EVT011', 'Nerio Green', 'Xe điện Nero - Nerio Green')
ON DUPLICATE KEY UPDATE 
    Model_Name = VALUES(Model_Name),
    description = VALUES(description);

-- Kiểm tra kết quả
SELECT 
    ID_Electric_Vehicle_Type,
    Model_Name,
    description,
    Battery_Type,
    price
FROM Electric_Vehicle_Type
ORDER BY ID_Electric_Vehicle_Type;

