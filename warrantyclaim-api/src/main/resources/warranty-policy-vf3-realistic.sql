

SET FOREIGN_KEY_CHECKS = 0;

-- Xóa policies cũ của VF3 (nếu có)
DELETE FROM Warranty_Policy_Electric_Vehicle_Type WHERE ID_Electric_Vehicle_Type = 'EVT001';
DELETE FROM Warranty_Policy WHERE ID_Warranty_Policy LIKE 'VF3-%';


INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-PIN-01', 'Bảo Hành Pin VF3 - Sử Dụng Cá Nhân', 'BATTERY',
'Bảo hành pin LFP gắn cố định của VF3. Thời hạn: 8 năm hoặc 160,000 km (tùy điều kiện nào đến trước). VinFast đảm bảo dung lượng pin ≥70% và không thấp hơn dung lượng pin cũ khi thay thế. Lưu ý: Tuổi thọ pin phụ thuộc vào cách sạc, dòng sạc, số lần phóng điện và nhiệt độ môi trường.', 
96);

-- WP-VF3-002: Pin sử dụng thương mại
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-PIN-02', 'Bảo Hành Pin VF3 - Xe Thương Mại', 'BATTERY',
'Bảo hành pin cho VF3 sử dụng thương mại (taxi, Grab, Be, xe cho thuê, xe đưa đón, xe giao hàng). Thời hạn: 3 năm hoặc 100,000 km (tùy điều kiện nào đến trước). Đảm bảo dung lượng ≥70%.', 
36);



INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-ACQ-01', 'Bảo Hành Ắc Quy 12V - VF3', 'PARTS_ONLY',
'Bảo hành ắc quy 12V phụ trợ trong 1 năm kể từ Ngày Kích Hoạt Bảo Hành (không giới hạn quãng đường). Thay thế miễn phí nếu hỏng do lỗi sản xuất.', 
12);



-- WP-VF3-004: Chống gỉ sét sử dụng cá nhân
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-RUST-01', 'Bảo Hành Chống Gỉ Sét VF3 - Cá Nhân', 'LIMITED',
'Bảo hành chống gỉ thủng kim loại trong 7 năm không giới hạn km. Áp dụng với tấm kim loại bị xuyên thủng do lỗi nguyên vật liệu hoặc lỗi lắp ráp. Trách nhiệm khách hàng: Rửa gầm xe nếu đi đường có muối/biển (ít nhất 1 tháng/lần), giữ lỗ thoát nước thông thoáng, sửa chữa ngay vết sơn hư hỏng, bảo vệ hàng hóa ăn mòn, lắp tấm chắn đá nếu đi đường sỏi.', 
84);

-- WP-VF3-005: Chống gỉ xe thương mại
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-RUST-02', 'Bảo Hành Chống Gỉ Sét VF3 - Thương Mại', 'LIMITED',
'Bảo hành chống gỉ cho xe thương mại. Thời hạn: 3 năm hoặc 100,000 km (tùy điều kiện nào đến trước). Yêu cầu bảo dưỡng và vệ sinh xe định kỳ có chứng từ.', 
36);

-- ============================================================================
-- 4. BẢO HÀNH LỐP XE
-- ============================================================================

INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-TIRE-01', 'Bảo Hành Lốp Xe - VF3', 'PARTS_ONLY',
'Lốp được trang bị theo xe (bao gồm lốp dự phòng nếu có) được bảo hành theo chính sách riêng của nhà sản xuất lốp. Áp dụng cho hư hỏng do lỗi nguyên vật liệu hoặc lỗi sản xuất/lưu kho. Việc đánh giá và quyết định bảo hành do nhà sản xuất lốp thực hiện.', 
24);

-- ============================================================================
-- 5. BẢO HÀNH SƠN NGOẠI THẤT
-- ============================================================================

-- WP-VF3-007: Sơn sử dụng cá nhân
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-PAINT-01', 'Bảo Hành Sơn Ngoại Thất VF3 - Cá Nhân', 'LIMITED',
'Bảo hành sơn ngoại thất vỏ xe trong 7 năm không giới hạn km. Bao gồm: bong tróc, phai màu, nứt do lỗi sản xuất. Không bao gồm: trầy xước do va chạm, thiếu bảo dưỡng, phơi nhiễm hóa chất.', 
84);

-- WP-VF3-008: Sơn xe thương mại
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-PAINT-02', 'Bảo Hành Sơn Ngoại Thất VF3 - Thương Mại', 'LIMITED',
'Bảo hành sơn cho xe thương mại. Thời hạn: 3 năm hoặc 100,000 km (tùy điều kiện nào đến trước).', 
36);

-- ============================================================================
-- 6. BẢO HÀNH CÁC BỘ PHẬN TREO
-- ============================================================================

-- WP-VF3-009: Hệ thống treo cá nhân
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-SUSP-01', 'Bảo Hành Bộ Phận Treo VF3 - Cá Nhân', 'PARTS_ONLY',
'Bảo hành các bộ phận treo: Bộ giảm xóc, Thanh ổn định, Cụm liên kết trên, Cánh tay điều khiển dưới, Khớp bi, Lắp thanh chống trên. Thời hạn: 5 năm hoặc 130,000 km (tùy điều kiện nào đến trước).', 
60);

-- WP-VF3-010: Hệ thống treo thương mại
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-SUSP-02', 'Bảo Hành Bộ Phận Treo VF3 - Thương Mại', 'PARTS_ONLY',
'Bảo hành hệ thống treo cho xe thương mại. Thời hạn: 3 năm hoặc 100,000 km (tùy điều kiện nào đến trước).', 
36);

-- ============================================================================
-- 7. BẢO HÀNH PHỤ TÙNG THAY THẾ CHÍNH HÃNG
-- ============================================================================

-- WP-VF3-011: Phụ tùng chính hãng (không bao gồm ắc quy và pin)
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-PART-01', 'Bảo Hành Phụ Tùng Chính Hãng - VF3', 'PARTS_ONLY',
'Phụ tùng thay thế chính hãng (không bao gồm ắc quy 12V và Pin cao áp) có thời hạn bảo hành 2 năm hoặc 40,000 km (tùy điều kiện nào đến trước) kể từ ngày mua. Điều kiện: Phụ tùng phải được thay thế tại XDV/ĐLPP của VinFast. Khách hàng cần lưu trữ hồ sơ sửa chữa, lệnh sửa chữa, quyết toán, hóa đơn.', 
24);

-- WP-VF3-012: Pin thay thế
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-PART-02', 'Bảo Hành Pin Thay Thế - VF3', 'BATTERY',
'Pin do khách hàng mua và được lắp đặt trên xe tại XDV/ĐLPP sau thời điểm giao xe có thời hạn bảo hành 4 năm hoặc 80,000 km (tùy điều kiện nào đến trước) kể từ ngày mua. Dung lượng đảm bảo ≥70%.', 
48);

-- WP-VF3-013: Ắc quy thay thế
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-PART-03', 'Bảo Hành Ắc Quy Thay Thế - VF3', 'PARTS_ONLY',
'Ắc quy 12V mua thay thế có thời hạn bảo hành 1 năm kể từ ngày mua (không giới hạn quãng đường).', 
12);

-- ============================================================================
-- 8. BẢO HÀNH PHỤ KIỆN CHÍNH HÃNG VINFAST
-- ============================================================================

-- WP-VF3-014: Phụ kiện không cố định
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-ACC-01', 'Bảo Hành Phụ Kiện Không Cố Định - VF3', 'PARTS_ONLY',
'Phụ kiện chính hãng không yêu cầu lắp đặt cố định trên xe (Bộ sạc di động, bộ dụng cụ sửa chữa lốp, thảm sàn, xích lốp, giá ngăn hành lý, biển tam giác cảnh báo, áo khoác phản quang...) có thời hạn bảo hành 2 năm không giới hạn km kể từ ngày mua. Điều kiện: Có hóa đơn mua hàng chính hãng.', 
24);

-- WP-VF3-015: Phụ kiện cố định - cá nhân
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-ACC-02', 'Bảo Hành Phụ Kiện Cố Định VF3 - Cá Nhân', 'PARTS_ONLY',
'Các phụ kiện bao gồm móc kéo xe (bao gồm điện), bậc lên xuống xe và giá đỡ hành lý có thời hạn bảo hành 5 năm không giới hạn km kể từ ngày mua. Điều kiện: Lắp đặt tại XDV/ĐLPP của VinFast, có hồ sơ lắp đặt, quyết toán và hóa đơn.', 
60);

-- WP-VF3-016: Phụ kiện cố định - thương mại
INSERT INTO Warranty_Policy (ID_Warranty_Policy, Policy_Name, Coverage_Type, Description, Coverage_Duration_Months)
VALUES ('VF3-ACC-03', 'Bảo Hành Phụ Kiện Cố Định VF3 - Thương Mại', 'PARTS_ONLY',
'Phụ kiện cố định cho xe thương mại có thời hạn bảo hành 3 năm không giới hạn km kể từ ngày mua.', 
36);

-- ============================================================================
-- 9. LIÊN KẾT VỚI LOẠI XE VF3 (EVT001)
-- ============================================================================
-- 9. LIÊN KẾT VỚI LOẠI XE VF3 (EVT001)
-- ============================================================================

INSERT INTO Warranty_Policy_Electric_Vehicle_Type (ID_Warranty_Policy, ID_Electric_Vehicle_Type)
VALUES 
    ('VF3-PIN-01', 'EVT001'),
    ('VF3-PIN-02', 'EVT001'),
    ('VF3-ACQ-01', 'EVT001'),
    ('VF3-RUST-01', 'EVT001'),
    ('VF3-RUST-02', 'EVT001'),
    ('VF3-TIRE-01', 'EVT001'),
    ('VF3-PAINT-01', 'EVT001'),
    ('VF3-PAINT-02', 'EVT001'),
    ('VF3-SUSP-01', 'EVT001'),
    ('VF3-SUSP-02', 'EVT001'),
    ('VF3-PART-01', 'EVT001'),
    ('VF3-PART-02', 'EVT001'),
    ('VF3-PART-03', 'EVT001'),
    ('VF3-ACC-01', 'EVT001'),
    ('VF3-ACC-02', 'EVT001'),
    ('VF3-ACC-03', 'EVT001');

-- ============================================================================
-- KIỂM TRA KẾT QUẢ
-- ============================================================================

SELECT 
    wp.ID_Warranty_Policy,
    wp.Policy_Name,
    wp.Coverage_Type,
    wp.Coverage_Duration_Months,
    LEFT(wp.Description, 80) as Description_Preview
FROM Warranty_Policy wp
WHERE wp.ID_Warranty_Policy LIKE 'VF3-%'
ORDER BY wp.ID_Warranty_Policy;

-- Đếm số policies cho VF3
SELECT 
    evt.Model_Name,
    COUNT(*) as Total_Policies
FROM Electric_Vehicle_Type evt
INNER JOIN Warranty_Policy_Electric_Vehicle_Type wpev 
    ON evt.ID_Electric_Vehicle_Type = wpev.ID_Electric_Vehicle_Type
WHERE evt.ID_Electric_Vehicle_Type = 'EVT001'
GROUP BY evt.Model_Name;
