
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================================
-- XÓA DATA CŨ TRƯỚC KHI INSERT MỚI
-- ============================================================================
DELETE FROM Warranty_Policy_Electric_Vehicle_Type;
DELETE FROM Warranty_Policy;

-- ============================================================================
-- INSERT WARRANTY POLICIES MỚI
-- ============================================================================

INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES


('WP001', 'Bảo Hành Toàn Diện 10 Năm', 'FULL',
 'Bảo hành toàn diện mọi lỗi sản xuất, lắp ráp và phần mềm trong điều kiện sử dụng bình thường. Bao gồm: động cơ điện, hộp số, hệ thống điện tử, khung xe, hệ thống phanh, treo và lái. Giới hạn: 10 năm hoặc không giới hạn km. Áp dụng cho: VF5, VF6, VF7, VF8, VF9 (sử dụng cá nhân).', 
 120),

('WP002', 'Bảo Hành Toàn Diện 7 Năm - VF3', 'FULL',
 'Bảo hành toàn diện dành riêng cho VF3 bao gồm mọi lỗi sản xuất, lắp ráp và phần mềm. Pin LFP cố định được bảo hành kèm theo. Giới hạn: 7 năm hoặc không giới hạn km. Chỉ áp dụng: VF3 (sử dụng cá nhân).',
 84),

('WP003', 'Bảo Hành Xe Thương Mại 3 Năm', 'LIMITED',
 'Dành cho xe sử dụng thương mại (taxi, dịch vụ vận chuyển, giao hàng, cho thuê). Chỉ bao gồm các linh kiện thiết yếu: động cơ điện, hộp số, hệ thống phanh chính, hệ thống lái. Giới hạn: 3 năm hoặc 100,000 km (tùy điều kiện nào đến trước). Điều kiện: phải bảo dưỡng định kỳ mỗi 10,000 km tại trung tâm uỷ quyền. Áp dụng cho: VF3, VF5, VF6, VF7, VF8, VF9.',
 36),


('WP004', 'Bảo Hành Pin Điện Áp Cao 10 Năm', 'BATTERY',
 'Bảo hành pin gốc (original battery pack) trong 10 năm hoặc 200,000 km (tùy điều kiện nào đến trước). Đảm bảo dung lượng pin duy trì ≥70% công suất ban đầu. Bao gồm: thay thế miễn phí nếu pin xuống dưới 70% trong thời gian bảo hành, sửa chữa các lỗi kỹ thuật của hệ thống quản lý pin (BMS). Điều kiện: sạc đúng cách theo hướng dẫn, không sử dụng sạc nhanh không chính hãng, bảo dưỡng định kỳ. Không bao gồm: hư hỏng do va chạm, ngập nước, sạc sai điện áp. Áp dụng cho: VF5, VF6, VF7, VF8, VF9 (VF3 không áp dụng vì pin cố định).',
 120),

('WP005', 'Bảo Hành Pin - Xe Thương Mại 3 Năm', 'BATTERY',
 'Bảo hành pin cho xe sử dụng thương mại. Đảm bảo dung lượng ≥70% trong 3 năm hoặc 100,000 km (tùy điều kiện nào đến trước). Yêu cầu bảo dưỡng định kỳ nghiêm ngặt mỗi 10,000 km. Áp dụng cho: VF5, VF6, VF7, VF8, VF9 (sử dụng thương mại).',
 36),

('WP006', 'Bảo Hành Pin Mua Riêng 4 Năm', 'BATTERY',
 'Dành cho pin được mua riêng và lắp đặt sau (không phải pin gốc kèm xe). Bảo hành 4 năm hoặc 80,000 km kể từ ngày mua pin. Dung lượng tối thiểu ≥70%. Điều kiện: phải lắp đặt tại trung tâm VinFast uỷ quyền, có hóa đơn mua hàng chính hãng. Áp dụng cho: VF5, VF6, VF7, VF8, VF9.',
 48),

('WP007', 'Bảo Hành Pin LFP Cố Định - VF3', 'BATTERY',
 'Dành riêng cho pin LFP cố định đi kèm xe VF3. Pin được bảo hành trong 7 năm không giới hạn km, đảm bảo dung lượng ≥70%. Pin không thể tháo rời và được bảo hành cùng với xe. Chỉ áp dụng: VF3.',
 84),


('WP008', 'Bảo Hành Ắc Quy 12V Phụ Trợ', 'PARTS_ONLY',
 'Bảo hành ắc quy 12V phụ trợ (dùng cho hệ thống điện phụ, đèn, khóa, cửa sổ) trong 1 năm không giới hạn km. Thay thế miễn phí nếu ắc quy hỏng do lỗi sản xuất. Không bao gồm: hư hỏng do để xe lâu không sử dụng, quên tắt đèn, ngắn mạch do tự lắp thiết bị điện. Áp dụng cho: VF3, VF5, VF6, VF7, VF8, VF9.',
 12),


('WP009', 'Bảo Hành Chống Gỉ Thủng Kim Loại 10 Năm', 'LIMITED',
 'Bảo vệ chống gỉ thủng kim loại (perforation) của khung xe, thân vỏ do lỗi sản xuất hoặc vật liệu. Giới hạn: 10 năm không giới hạn km. Không bao gồm: gỉ bề mặt, gỉ do va chạm, trầy xước, thiếu bảo dưỡng. Điều kiện: xe phải được rửa định kỳ (đặc biệt khi đi vùng biển, đường muối), kiểm tra và xử lý gỉ sớm. Áp dụng cho: VF5, VF6, VF7, VF8, VF9 (VF3 không áp dụng).',
 120),

('WP010', 'Bảo Hành Chống Gỉ - Xe Thương Mại', 'LIMITED',
 'Bảo hành chống gỉ thủng cho xe thương mại. Giới hạn: 3 năm hoặc 100,000 km. Yêu cầu vệ sinh xe định kỳ có chứng từ. Áp dụng cho: VF5, VF6, VF7, VF8, VF9 (sử dụng thương mại).',
 36),


('WP011', 'Bảo Hành Sơn Ngoại Thất 10 Năm', 'LIMITED',
 'Bảo hành lớp sơn ngoại thất chống bong tróc, phai màu, nứt do lỗi sản xuất hoặc vật liệu trong điều kiện sử dụng bình thường. Giới hạn: 10 năm không giới hạn km. Không bao gồm: trầy xước do va chạm, đỗ xe dưới nắng gắt lâu ngày, phơi nhiễm hóa chất, thiếu chăm sóc. Điều kiện: đánh bóng, phủ ceramic định kỳ được khuyến nghị. Áp dụng cho: VF5, VF6, VF7, VF8, VF9 (VF3 không áp dụng).',
 120),

('WP012', 'Bảo Hành Sơn - Xe Thương Mại', 'LIMITED',
 'Bảo hành sơn cho xe thương mái. Giới hạn: 3 năm hoặc 100,000 km. Không bao gồm: quảng cáo decal, dán sticker. Áp dụng cho: VF5, VF6, VF7, VF8, VF9.',
 36),


('WP013', 'Bảo Hành Hệ Thống Treo 5 Năm', 'PARTS_ONLY',
 'Bảo hành các bộ phận hệ thống treo: giảm xóc (shock absorbers), cần treo (control arms), thanh cân bằng (stabilizer bars), cao su chống đỡ (bushings), giảm xóc lò xo (struts). Giới hạn: 5 năm hoặc 130,000 km (tùy điều kiện nào đến trước). Không bao gồm: hao mòn tự nhiên do sử dụng đường xấu, quá tải, độ cao gầm bị thay đổi. Điều kiện: kiểm tra định kỳ mỗi 20,000 km. Áp dụng cho: VF5, VF6, VF7, VF8, VF9.',
 60),

('WP014', 'Bảo Hành Hệ Thống Treo - Xe Thương Mại', 'PARTS_ONLY',
 'Bảo hành hệ thống treo cho xe thương mại chở nặng thường xuyên. Giới hạn: 3 năm hoặc 100,000 km. Yêu cầu kiểm tra mỗi 10,000 km. Áp dụng cho: VF5, VF6, VF7, VF8, VF9.',
 36),


('WP015', 'Bảo Hành Phụ Tùng Chính Hãng 2 Năm', 'PARTS_ONLY',
 'Bảo hành các phụ tùng thay thế chính hãng VinFast. Giới hạn: 2 năm hoặc 40,000 km kể từ ngày mua phụ tùng (tùy điều kiện nào đến trước). Điều kiện: phải có hóa đơn mua hàng, lắp đặt tại trung tâm uỷ quyền. Không áp dụng: phụ tùng tiêu hao (phanh, lốp, gạt mưa, bóng đèn). Áp dụng cho: VF3, VF5, VF6, VF7, VF8, VF9.',
 24),


('WP016', 'Bảo Hành Phụ Kiện Chính Thức 2 Năm', 'PARTS_ONLY',
 'Bảo hành các phụ kiện chính thức VinFast không gắn cố định vào xe (camera hành trình, sạc dự phòng, túi đựng đồ, thảm lót sàn, v.v.). Giới hạn: 2 năm không giới hạn km. Điều kiện: có hóa đơn mua hàng chính hãng. Áp dụng cho: VF3, VF5, VF6, VF7, VF8, VF9.',
 24),

('WP017', 'Bảo Hành Phụ Kiện Gắn Cố Định 5 Năm', 'PARTS_ONLY',
 'Bảo hành phụ kiện gắn cố định trên xe: móc kéo (tow hooks), bậc lên xuống (running boards), giá nóc (roof racks), ốp body kit, ốp cản trước/sau. Giới hạn: 5 năm không giới hạn km. Điều kiện: lắp đặt tại trung tâm VinFast uỷ quyền. Áp dụng cho: VF5, VF6, VF7, VF8, VF9.',
 60),

('WP018', 'Bảo Hành Phụ Kiện - Xe Thương Mại', 'PARTS_ONLY',
 'Bảo hành phụ kiện cho xe thương mại (barie, giá chở hàng, đèn cảnh báo). Giới hạn: 3 năm không giới hạn km. Áp dụng cho: VF5, VF6, VF7, VF8, VF9.',
 36),


('WP019', 'Bảo Hành Hệ Thống Truyền Động 8 Năm', 'POWERTRAIN',
 'Bảo hành chuyên sâu cho hệ thống truyền động điện: động cơ điện (electric motor), bộ nghịch lưu (inverter), hộp giảm tốc (gearbox/reducer), trục truyền (drive shafts), vi sai (differential). Giới hạn: 8 năm hoặc 180,000 km (tùy điều kiện nào đến trước). Bao gồm: sửa chữa hoặc thay thế hoàn toàn nếu hỏng. Không bao gồm: hao mòn ly hợp (nếu có), dầu nhớt. Áp dụng cho: VF5, VF6, VF7, VF8, VF9.',
 96),


('WP020', 'Bảo Hành Mở Rộng 5 Năm', 'EXTENDED',
 'Gói bảo hành mở rộng tùy chọn sau khi hết bảo hành cơ bản. Bao gồm: động cơ, hộp số, hệ thống điện chính. Giới hạn: 5 năm hoặc 100,000 km (tùy điều kiện nào đến trước). Điều kiện: xe phải được kiểm tra và đánh giá trước khi gia hạn, không có lỗi kỹ thuật nghiêm trọng, phí bảo hành: 8-12% giá trị xe. Áp dụng cho: VF5, VF6, VF7, VF8, VF9.',
 60),


('WP021', 'Hỗ Trợ Cứu Hộ 24/7 - 10 Năm', 'LIMITED',
 'Dịch vụ cứu hộ miễn phí 24/7 cho các trường hợp hỏng hóc liên quan đến bảo hành. Bao gồm: kéo xe miễn phí đến trung tâm VinFast gần nhất (trong bán kính 200km), hỗ trợ sạc pin khẩn cấp, thay lốp dự phòng, mở khóa xe, tiếp nhiên liệu (đối với xe hybrid nếu có). Giới hạn: 10 năm không giới hạn km. Không bao gồm: tai nạn, hết xăng/pin do quên sạc. Áp dụng cho: VF5, VF6, VF7, VF8, VF9 (VF3 không áp dụng).',
 120),

('WP022', 'Hỗ Trợ Cứu Hộ - VF3', 'LIMITED',
 'Dịch vụ cứu hộ cơ bản cho VF3. Bao gồm: kéo xe miễn phí trong bán kính 100km, hỗ trợ sạc pin. Giới hạn: 7 năm. Chỉ áp dụng: VF3.',
 84),


('WP023', 'Loại Trừ: Hư Hỏng Do Sử Dụng Sai', 'NONE',
 'Các trường hợp KHÔNG được bảo hành: sử dụng sai mục đích (đua xe, off-road không phù hợp), tai nạn, va chạm, ngập nước, cháy nổ do bên ngoài, sửa chữa/cải tạo không uỷ quyền, sử dụng phụ tùng không chính hãng, thiếu bảo dưỡng định kỳ (không có sổ bảo dưỡng), thay đổi số VIN hoặc giấy tờ xe. Áp dụng cho: VF3, VF5, VF6, VF7, VF8, VF9.',
 0),

('WP024', 'Loại Trừ: Vật Phẩm Tiêu Hao', 'NONE',
 'Các vật phẩm tiêu hao KHÔNG được bảo hành: má phanh (brake pads), đĩa phanh (rotors sau 20,000km), bóng đèn, cầu chì (fuses), gạt mưa (wiper blades), lốp xe, dầu nhớt/dung dịch (coolant, brake fluid, washer fluid), pin chìa khóa, bộ lọc không khí cabin, bộ lọc điều hòa. Áp dụng cho: VF3, VF5, VF6, VF7, VF8, VF9.',
 0),

('WP025', 'Loại Trừ: Hao Mòn Tự Nhiên', 'NONE',
 'Hao mòn tự nhiên do sử dụng lâu dài KHÔNG được bảo hành: bề mặt ghế ngồi bị nhăn/sờn, nội thất bị mòn, trầy xước nhỏ, tiếng kêu cọt kẹt nhỏ không ảnh hưởng chức năng, mùi hôi trong xe, các bộ phận cao su bị cứng/nứt theo thời gian. Áp dụng cho: VF3, VF5, VF6, VF7, VF8, VF9.',
 0);



INSERT INTO Warranty_Policy_Electric_Vehicle_Type (Warranty_Policy_ID, Vehicle_Type_ID)
VALUES

('WP002', 'EVT001'),  -- Full Vehicle 7 years
('WP003', 'EVT001'),  -- Commercial use
('WP007', 'EVT001'),  -- Fixed LFP Battery
('WP008', 'EVT001'),  -- 12V battery
('WP015', 'EVT001'),  -- Replacement parts
('WP016', 'EVT001'),  -- Official accessories
('WP022', 'EVT001'),  -- Roadside assistance VF3
('WP023', 'EVT001'),  -- Exclusions: misuse
('WP024', 'EVT001'),  -- Exclusions: consumables
('WP025', 'EVT001'),  -- Exclusions: wear and tear


('WP001', 'EVT002'),  -- Full Vehicle 10 years
('WP003', 'EVT002'),  -- Commercial use
('WP004', 'EVT002'),  -- High voltage battery 10 years
('WP005', 'EVT002'),  -- Battery commercial
('WP006', 'EVT002'),  -- Purchased battery
('WP008', 'EVT002'),  -- 12V battery
('WP009', 'EVT002'),  -- Corrosion 10 years
('WP010', 'EVT002'),  -- Corrosion commercial
('WP011', 'EVT002'),  -- Paint 10 years
('WP012', 'EVT002'),  -- Paint commercial
('WP013', 'EVT002'),  -- Suspension 5 years
('WP014', 'EVT002'),  -- Suspension commercial
('WP015', 'EVT002'),  -- Replacement parts
('WP016', 'EVT002'),  -- Official accessories
('WP017', 'EVT002'),  -- Mounted accessories
('WP018', 'EVT002'),  -- Commercial accessories
('WP019', 'EVT002'),  -- Powertrain 8 years
('WP020', 'EVT002'),  -- Extended warranty
('WP021', 'EVT002'),  -- Roadside assistance
('WP023', 'EVT002'),  -- Exclusions: misuse
('WP024', 'EVT002'),  -- Exclusions: consumables
('WP025', 'EVT002'),  -- Exclusions: wear and tear


('WP001', 'EVT003'),  -- Full Vehicle 10 years
('WP003', 'EVT003'),  -- Commercial use
('WP004', 'EVT003'),  -- High voltage battery 10 years
('WP005', 'EVT003'),  -- Battery commercial
('WP006', 'EVT003'),  -- Purchased battery
('WP008', 'EVT003'),  -- 12V battery
('WP009', 'EVT003'),  -- Corrosion 10 years
('WP010', 'EVT003'),  -- Corrosion commercial
('WP011', 'EVT003'),  -- Paint 10 years
('WP012', 'EVT003'),  -- Paint commercial
('WP013', 'EVT003'),  -- Suspension 5 years
('WP014', 'EVT003'),  -- Suspension commercial
('WP015', 'EVT003'),  -- Replacement parts
('WP016', 'EVT003'),  -- Official accessories
('WP017', 'EVT003'),  -- Mounted accessories
('WP018', 'EVT003'),  -- Commercial accessories
('WP019', 'EVT003'),  -- Powertrain 8 years
('WP020', 'EVT003'),  -- Extended warranty
('WP021', 'EVT003'),  -- Roadside assistance
('WP023', 'EVT003'),  -- Exclusions: misuse
('WP024', 'EVT003'),  -- Exclusions: consumables
('WP025', 'EVT003'),  -- Exclusions: wear and tear


('WP001', 'EVT004'),  -- Full Vehicle 10 years
('WP003', 'EVT004'),  -- Commercial use
('WP004', 'EVT004'),  -- High voltage battery 10 years
('WP005', 'EVT004'),  -- Battery commercial
('WP006', 'EVT004'),  -- Purchased battery
('WP008', 'EVT004'),  -- 12V battery
('WP009', 'EVT004'),  -- Corrosion 10 years
('WP010', 'EVT004'),  -- Corrosion commercial
('WP011', 'EVT004'),  -- Paint 10 years
('WP012', 'EVT004'),  -- Paint commercial
('WP013', 'EVT004'),  -- Suspension 5 years
('WP014', 'EVT004'),  -- Suspension commercial
('WP015', 'EVT004'),  -- Replacement parts
('WP016', 'EVT004'),  -- Official accessories
('WP017', 'EVT004'),  -- Mounted accessories
('WP018', 'EVT004'),  -- Commercial accessories
('WP019', 'EVT004'),  -- Powertrain 8 years
('WP020', 'EVT004'),  -- Extended warranty
('WP021', 'EVT004'),  -- Roadside assistance
('WP023', 'EVT004'),  -- Exclusions: misuse
('WP024', 'EVT004'),  -- Exclusions: consumables
('WP025', 'EVT004'),  -- Exclusions: wear and tear


('WP001', 'EVT005'),  -- Full Vehicle 10 years
('WP003', 'EVT005'),  -- Commercial use
('WP004', 'EVT005'),  -- High voltage battery 10 years
('WP005', 'EVT005'),  -- Battery commercial
('WP006', 'EVT005'),  -- Purchased battery
('WP008', 'EVT005'),  -- 12V battery
('WP009', 'EVT005'),  -- Corrosion 10 years
('WP010', 'EVT005'),  -- Corrosion commercial
('WP011', 'EVT005'),  -- Paint 10 years
('WP012', 'EVT005'),  -- Paint commercial
('WP013', 'EVT005'),  -- Suspension 5 years
('WP014', 'EVT005'),  -- Suspension commercial
('WP015', 'EVT005'),  -- Replacement parts
('WP016', 'EVT005'),  -- Official accessories
('WP017', 'EVT005'),  -- Mounted accessories
('WP018', 'EVT005'),  -- Commercial accessories
('WP019', 'EVT005'),  -- Powertrain 8 years
('WP020', 'EVT005'),  -- Extended warranty
('WP021', 'EVT005'),  -- Roadside assistance
('WP023', 'EVT005'),  -- Exclusions: misuse
('WP024', 'EVT005'),  -- Exclusions: consumables
('WP025', 'EVT005'),  -- Exclusions: wear and tear


('WP001', 'EVT006'),  -- Full Vehicle 10 years
('WP003', 'EVT006'),  -- Commercial use
('WP004', 'EVT006'),  -- High voltage battery 10 years
('WP005', 'EVT006'),  -- Battery commercial
('WP006', 'EVT006'),  -- Purchased battery
('WP008', 'EVT006'),  -- 12V battery
('WP009', 'EVT006'),  -- Corrosion 10 years
('WP010', 'EVT006'),  -- Corrosion commercial
('WP011', 'EVT006'),  -- Paint 10 years
('WP012', 'EVT006'),  -- Paint commercial
('WP013', 'EVT006'),  -- Suspension 5 years
('WP014', 'EVT006'),  -- Suspension commercial
('WP015', 'EVT006'),  -- Replacement parts
('WP016', 'EVT006'),  -- Official accessories
('WP017', 'EVT006'),  -- Mounted accessories
('WP018', 'EVT006'),  -- Commercial accessories
('WP019', 'EVT006'),  -- Powertrain 8 years
('WP020', 'EVT006'),  -- Extended warranty
('WP021', 'EVT006'),  -- Roadside assistance
('WP023', 'EVT006'),  -- Exclusions: misuse
('WP024', 'EVT006'),  -- Exclusions: consumables
('WP025', 'EVT006');  -- Exclusions: wear and tear

-- Enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;


SELECT 
    wp.ID_Warranty_Policy,
    wp.Policy_Name,
    wp.Coverage_Type,
    wp.Coverage_Duration_Months,
    LEFT(wp.Description, 80) as Description_Preview
FROM Warranty_Policy wp
ORDER BY wp.ID_Warranty_Policy;

SELECT 
    evt.ID_Electric_Vehicle_Type,
    evt.Model_Name,
    COUNT(wpev.Warranty_Policy_ID) as Policy_Count
FROM Electric_Vehicle_Type evt
LEFT JOIN Warranty_Policy_Electric_Vehicle_Type wpev 
    ON evt.ID_Electric_Vehicle_Type = wpev.Vehicle_Type_ID
GROUP BY evt.ID_Electric_Vehicle_Type, evt.Model_Name
ORDER BY evt.ID_Electric_Vehicle_Type;

SELECT 
    wp.ID_Warranty_Policy,
    wp.Policy_Name,
    wp.Coverage_Type,
    wp.Coverage_Duration_Months
FROM Warranty_Policy wp
INNER JOIN Warranty_Policy_Electric_Vehicle_Type wpev 
    ON wp.ID_Warranty_Policy = wpev.Warranty_Policy_ID
WHERE wpev.Vehicle_Type_ID = 'EVT005'  -- VF8
ORDER BY wp.Coverage_Type, wp.Coverage_Duration_Months DESC;
