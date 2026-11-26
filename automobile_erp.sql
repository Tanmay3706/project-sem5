

DROP DATABASE IF EXISTS stockdrive;
CREATE DATABASE stockdrive CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stockdrive;



CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','MANAGER','STAFF') NOT NULL DEFAULT 'STAFF',
    status ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE suppliers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100),
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(10),
    status ENUM('ACTIVE','PENDING','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(150) NOT NULL,
    category VARCHAR(100) NOT NULL,
    brand VARCHAR(100),
    part_number VARCHAR(100) NOT NULL UNIQUE,
    selling_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    quantity INT NOT NULL DEFAULT 0,
    minimum_stock INT NOT NULL DEFAULT 0,
    supplier_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_supplier FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id) ON DELETE SET NULL ON UPDATE CASCADE
);



CREATE TABLE purchase_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    supplier_id BIGINT NOT NULL,
    order_date DATE NOT NULL,
    status ENUM('PENDING','APPROVED','COMPLETED','CANCELLED')
        NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    notes VARCHAR(500),
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_purchase_supplier FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_purchase_user FOREIGN KEY (created_by)
        REFERENCES users(id) ON DELETE SET NULL ON UPDATE CASCADE
);



CREATE TABLE purchase_order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    purchase_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(12,2)
        GENERATED ALWAYS AS (quantity * unit_price) STORED,
    CONSTRAINT fk_order_item_order FOREIGN KEY (purchase_order_id)
        REFERENCES purchase_orders(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id)
        REFERENCES products(id) ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE stock_movements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    movement_type ENUM('IN','OUT','ADJUSTMENT') NOT NULL,
    quantity INT NOT NULL,
    reference_type VARCHAR(50),
    reference_id BIGINT,
    remarks VARCHAR(255),
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_product FOREIGN KEY (product_id)
        REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_stock_user FOREIGN KEY (created_by)
        REFERENCES users(id) ON DELETE SET NULL ON UPDATE CASCADE
);



CREATE INDEX idx_product_name ON products(product_name);
CREATE INDEX idx_product_category ON products(category);
CREATE INDEX idx_supplier_name ON suppliers(supplier_name);
CREATE INDEX idx_purchase_order_date ON purchase_orders(order_date);
CREATE INDEX idx_purchase_order_status ON purchase_orders(status);
CREATE INDEX idx_stock_created_at ON stock_movements(created_at);



INSERT INTO users (username,email,phone,password_hash,role,status) VALUES
('admin','admin@stockdrive.com','9876543210',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','ADMIN','ACTIVE'),
('manager01','manager@stockdrive.com','9876501234',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','MANAGER','ACTIVE'),
('staff01','staff@stockdrive.com','9865432101',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','STAFF','ACTIVE'),
('staff02','staff2@stockdrive.com','9856321470',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','STAFF','INACTIVE');



INSERT INTO suppliers
(supplier_name,contact_person,phone,email,address,city,state,pincode,status)
VALUES
('ABC Auto Parts','Rajesh Sharma','9876543210','abc@gmail.com','Industrial Area','Pune','Maharashtra','411019','ACTIVE'),
('XYZ Motors','Amit Patil','9876501234','xyz@gmail.com','MIDC Road','Mumbai','Maharashtra','400001','ACTIVE'),
('Auto Parts India','Suresh Kumar','9988776655','autoparts@gmail.com','Auto Market','Delhi','Delhi','110001','PENDING'),
('Premium Car Parts','Vikram Singh','9123456780','premium@gmail.com','Gangapur Road','Nashik','Maharashtra','422005','ACTIVE'),
('Metro Auto Components','Arjun Mehta','9810011005','metroauto@gmail.com','Industrial Estate','Pune','Maharashtra','411019','ACTIVE'),
('Prime Motor Supplies','Rohit Kulkarni','9810011006','primemotor@gmail.com','Bhosari MIDC','Pune','Maharashtra','411026','ACTIVE'),
('Reliable Auto Solutions','Karan Shah','9810011007','reliableauto@gmail.com','Andheri Industrial Area','Mumbai','Maharashtra','400053','ACTIVE'),
('National Car Components','Vivek Rao','9810011008','nationalcar@gmail.com','Peenya Industrial Area','Bangalore','Karnataka','560058','ACTIVE'),
('Elite Automotive Parts','Manish Verma','9810011009','eliteauto@gmail.com','Okhla Industrial Area','Delhi','Delhi','110020','ACTIVE'),
('Supreme Auto Spares','Nitin Joshi','9810011010','supremeauto@gmail.com','Vashi Industrial Area','Navi Mumbai','Maharashtra','400703','ACTIVE'),
('Rapid Motors Supply','Akash Jain','9810011011','rapidmotors@gmail.com','Vatva Industrial Estate','Ahmedabad','Gujarat','382445','ACTIVE'),
('Global Auto Parts','Rakesh Nair','9810011012','globalauto@gmail.com','Ambattur Industrial Estate','Chennai','Tamil Nadu','600058','ACTIVE'),
('PrimeDrive Components','Sanjay More','9810011013','primedrive@gmail.com','Chinchwad MIDC','Pune','Maharashtra','411019','ACTIVE'),
('AutoTech Distributors','Deepak Yadav','9810011014','autotech@gmail.com','Faridabad Industrial Area','Faridabad','Haryana','121001','ACTIVE'),
('Motorline India','Prakash Iyer','9810011015','motorline@gmail.com','Whitefield Industrial Area','Bangalore','Karnataka','560066','ACTIVE'),
('Velocity Auto Parts','Sameer Khan','9810011016','velocityauto@gmail.com','Kurla Industrial Estate','Mumbai','Maharashtra','400070','ACTIVE'),
('Classic Motor Spares','Harish Patil','9810011017','classicmotor@gmail.com','Waluj MIDC','Aurangabad','Maharashtra','431136','ACTIVE'),
('AutoWorld Components','Rahul Deshmukh','9810011018','autoworld@gmail.com','Hinjewadi Industrial Area','Pune','Maharashtra','411057','ACTIVE'),
('DriveLine Auto Supplies','Mohit Agarwal','9810011019','driveline@gmail.com','Manesar Industrial Area','Gurgaon','Haryana','122051','ACTIVE'),
('MaxTorque Parts','Siddharth Rao','9810011020','maxtorque@gmail.com','Rajajinagar Industrial Area','Bangalore','Karnataka','560010','ACTIVE'),
('RoadMaster Components','Yash Thakur','9810011021','roadmaster@gmail.com','Nashik Industrial Area','Nashik','Maharashtra','422007','ACTIVE'),
('AutoCare Distributors','Amit Joshi','9810011022','autocare@gmail.com','Ludhiana Industrial Area','Ludhiana','Punjab','141003','ACTIVE'),
('GearPoint India','Varun Singh','9810011023','gearpoint@gmail.com','Noida Industrial Area','Noida','Uttar Pradesh','201301','ACTIVE'),
('MotorHub Supplies','Aditya Kapoor','9810011024','motorhub@gmail.com','Howrah Industrial Area','Kolkata','West Bengal','711101','ACTIVE'),
('Fusion Auto Components','Neeraj Gupta','9810011025','fusionauto@gmail.com','Indore Industrial Area','Indore','Madhya Pradesh','452015','ACTIVE');



INSERT INTO products
(product_name,category,brand,part_number,selling_price,quantity,minimum_stock,supplier_id)
VALUES
('Brake Pad','Braking System','Bosch','BP101',750.00,120,20,1),
('Oil Filter','Filters','Castrol','OF102',250.00,8,15,2),
('Air Filter','Filters','Bosch','AF103',320.00,60,10,3),
('Headlight','Lighting','Philips','HL104',1200.00,0,10,4),
('Clutch Plate','Transmission & Clutch','Valeo','CP105',3500.00,25,8,1),
('Spark Plug','Ignition System','NGK','SP106',450.00,50,30,2),
('Radiator','Cooling System','Mahle','RD107',5500.00,4,10,3);


INSERT INTO products
(product_name,category,brand,part_number,selling_price,quantity,minimum_stock,supplier_id)
WITH RECURSIVE numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 193
),
product_types AS (
    SELECT 1 AS type_id,'Engine Mount' AS part_name,'Engine' AS category
    UNION ALL SELECT 2,'Timing Belt','Engine'
    UNION ALL SELECT 3,'Piston Ring Set','Engine'
    UNION ALL SELECT 4,'Brake Disc','Braking System'
    UNION ALL SELECT 5,'Brake Caliper','Braking System'
    UNION ALL SELECT 6,'Brake Shoe Set','Braking System'
    UNION ALL SELECT 7,'Fuel Pump','Fuel System'
    UNION ALL SELECT 8,'Fuel Injector','Fuel System'
    UNION ALL SELECT 9,'Fuel Filter','Filters'
    UNION ALL SELECT 10,'Oil Filter','Filters'
    UNION ALL SELECT 11,'Air Filter','Filters'
    UNION ALL SELECT 12,'Cabin Filter','Filters'
    UNION ALL SELECT 13,'Clutch Plate','Transmission & Clutch'
    UNION ALL SELECT 14,'Clutch Release Bearing','Transmission & Clutch'
    UNION ALL SELECT 15,'Radiator','Cooling System'
    UNION ALL SELECT 16,'Cooling Fan','Cooling System'
    UNION ALL SELECT 17,'Thermostat','Cooling System'
    UNION ALL SELECT 18,'Alternator','Electrical System'
    UNION ALL SELECT 19,'Starter Motor','Electrical System'
    UNION ALL SELECT 20,'Battery','Electrical System'
    UNION ALL SELECT 21,'Shock Absorber','Suspension & Steering'
    UNION ALL SELECT 22,'Ball Joint','Suspension & Steering'
    UNION ALL SELECT 23,'Steering Rack','Suspension & Steering'
    UNION ALL SELECT 24,'Spark Plug','Ignition System'
    UNION ALL SELECT 25,'Ignition Coil','Ignition System'
    UNION ALL SELECT 26,'Headlight','Lighting'
    UNION ALL SELECT 27,'Fog Lamp','Lighting'
    UNION ALL SELECT 28,'Tail Light','Lighting'
    UNION ALL SELECT 29,'Wheel Bearing','Wheels & Bearings'
    UNION ALL SELECT 30,'Wheel Hub Assembly','Wheels & Bearings'
    UNION ALL SELECT 31,'Wiper Motor','Wipers & Accessories'
    UNION ALL SELECT 32,'Wiper Blade','Wipers & Accessories'
    UNION ALL SELECT 33,'Gearbox Mount','Transmission & Clutch'
    UNION ALL SELECT 34,'Drive Shaft','Transmission & Clutch'
    UNION ALL SELECT 35,'Exhaust Muffler','Exhaust System'
    UNION ALL SELECT 36,'Oxygen Sensor','Exhaust System'
)
SELECT
    CONCAT(pt.part_name,' ',LPAD(n.n,3,'0')),
    pt.category,
    CASE MOD(n.n,10)
        WHEN 0 THEN 'Bosch'
        WHEN 1 THEN 'Valeo'
        WHEN 2 THEN 'Brembo'
        WHEN 3 THEN 'NGK'
        WHEN 4 THEN 'Mahle'
        WHEN 5 THEN 'Denso'
        WHEN 6 THEN 'Philips'
        WHEN 7 THEN 'SKF'
        WHEN 8 THEN 'TRW'
        ELSE 'Continental'
    END,
    CONCAT('SDP',LPAD(n.n,5,'0')),
    CAST(250 + MOD(n.n * 317,7500) AS DECIMAL(10,2)),
    CASE
        WHEN MOD(n.n,20)=0 THEN 0
        WHEN MOD(n.n,13)=0 THEN 3
        WHEN MOD(n.n,9)=0 THEN 7
        ELSE 20 + MOD(n.n * 17,100)
    END,
    10 + MOD(n.n,20),
    1 + MOD(n.n-1,25)
FROM numbers n
JOIN product_types pt ON pt.type_id = MOD(n.n-1,36)+1;


INSERT INTO purchase_orders
(order_number,supplier_id,order_date,status,total_amount,notes,created_by)
VALUES
('PO001',1,'2026-08-18','COMPLETED',37500.00,'Brake pad stock replenishment',1),
('PO002',2,'2026-08-17','PENDING',25000.00,'Oil filter requirement',1),
('PO003',3,'2026-08-16','APPROVED',24000.00,'Air filter order',2),
('PO004',4,'2026-08-15','PENDING',24000.00,'Headlight stock requirement',2);



INSERT INTO purchase_order_items
(purchase_order_id,product_id,quantity,unit_price)
VALUES
(1,1,50,750.00),
(2,2,100,250.00),
(3,3,75,320.00),
(4,4,20,1200.00);



INSERT INTO stock_movements
(product_id,movement_type,quantity,reference_type,reference_id,remarks,created_by)
VALUES
(1,'IN',50,'PURCHASE_ORDER',1,'Brake pads received',1),
(2,'IN',100,'PURCHASE_ORDER',2,'Oil filters received',1),
(3,'IN',75,'PURCHASE_ORDER',3,'Air filters received',2),
(4,'OUT',5,'SALE',NULL,'Headlight sold',3),
(5,'OUT',3,'SALE',NULL,'Clutch plates sold',3);



SELECT 'Users' AS item,COUNT(*) AS total FROM users
UNION ALL
SELECT 'Suppliers',COUNT(*) FROM suppliers
UNION ALL
SELECT 'Products',COUNT(*) FROM products
UNION ALL
SELECT 'Purchase Orders',COUNT(*) FROM purchase_orders
UNION ALL
SELECT 'Purchase Order Items',COUNT(*) FROM purchase_order_items
UNION ALL
SELECT 'Stock Movements',COUNT(*) FROM stock_movements;

SELECT category,COUNT(*) AS total_products
FROM products
GROUP BY category
ORDER BY category;

SELECT id,product_name,category,brand,part_number,
       selling_price,quantity,minimum_stock,supplier_id
FROM products
ORDER BY category,product_name;

SELECT id,product_name,category
FROM products
WHERE category='Lighting'
ORDER BY product_name;

SELECT COUNT(*) AS low_stock_products
FROM products
WHERE quantity <= minimum_stock;

SHOW TABLES;



USE stockdrive;

SELECT COUNT(*) AS total_products FROM products;
SELECT COUNT(*) AS total_suppliers FROM suppliers;
SHOW TABLES;