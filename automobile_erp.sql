
DROP DATABASE IF EXISTS automobile_erp;
CREATE DATABASE automobile_erp;

USE automobile_erp;


CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin','Manager','Staff') DEFAULT 'Staff',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100),
    address TEXT,
    city VARCHAR(50),
    state VARCHAR(50),
    pincode VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE inventory (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    brand VARCHAR(50),
    part_number VARCHAR(50) UNIQUE,
    description TEXT,
    purchase_price DECIMAL(10,2),
    selling_price DECIMAL(10,2),
    quantity INT DEFAULT 0,
    minimum_stock INT DEFAULT 10,
    supplier_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (supplier_id)
    REFERENCES suppliers(supplier_id)
    ON DELETE SET NULL
);


CREATE TABLE purchase_orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id INT NOT NULL,
    order_date DATE,
    total_amount DECIMAL(12,2),
    status ENUM('Pending','Completed','Cancelled')
    DEFAULT 'Pending',

    FOREIGN KEY (supplier_id)
    REFERENCES suppliers(supplier_id)
);



CREATE TABLE purchase_order_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2),

    FOREIGN KEY(order_id)
    REFERENCES purchase_orders(order_id)
    ON DELETE CASCADE,

    FOREIGN KEY(product_id)
    REFERENCES inventory(product_id)
);


CREATE TABLE stock_alerts (
    alert_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    quantity_available INT,
    minimum_required INT,
    alert_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(product_id)
    REFERENCES inventory(product_id)
    ON DELETE CASCADE
);


CREATE TABLE login_history (
    login_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(user_id)
    REFERENCES users(user_id)
    ON DELETE CASCADE
);



INSERT INTO users
(username,email,phone,password,role)
VALUES
('admin','admin@gmail.com','9876543210','admin123','Admin'),

('manager','manager@gmail.com','9876543211','manager123','Manager'),

('staff','staff@gmail.com','9876543212','staff123','Staff');


INSERT INTO suppliers
(supplier_name,contact_person,phone,email,address,city,state,pincode)
VALUES

('ABC Auto Parts','Rahul Sharma','9876500001','abc@gmail.com','MIDC Industrial Area','Pune','Maharashtra','411001'),

('XYZ Motors','Amit Patil','9876500002','xyz@gmail.com','Ring Road','Mumbai','Maharashtra','400001');


INSERT INTO inventory
(product_name,category,brand,part_number,description,purchase_price,selling_price,quantity,minimum_stock,supplier_id)
VALUES

('Brake Pad','Brake System','Bosch','BP101','Front Brake Pad',500,750,120,20,1),

('Oil Filter','Engine','Castrol','OF102','Premium Oil Filter',150,250,80,15,2),

('Air Filter','Engine','Bosch','AF103','Air Filter',200,320,60,10,1),

('Headlight','Electrical','Philips','HL104','LED Headlight',800,1200,30,10,2);



INSERT INTO purchase_orders
(supplier_id,order_date,total_amount,status)
VALUES

(1,'2026-08-01',25000,'Completed'),

(2,'2026-08-02',18000,'Pending');



INSERT INTO purchase_order_items
(order_id,product_id,quantity,price)
VALUES

(1,1,50,500),

(1,3,25,200),

(2,2,60,150),

(2,4,10,800);



INSERT INTO stock_alerts
(product_id,quantity_available,minimum_required)
VALUES

(4,5,10),

(3,8,10);



INSERT INTO login_history(user_id)
VALUES
(1),
(2),
(3);



-- View Users
SELECT * FROM users;

-- View Suppliers
SELECT * FROM suppliers;

-- View Inventory
SELECT * FROM inventory;

-- View Purchase Orders
SELECT * FROM purchase_orders;

-- View Purchase Order Details
SELECT
po.order_id,
s.supplier_name,
i.product_name,
poi.quantity,
poi.price
FROM purchase_order_items poi
JOIN purchase_orders po
ON poi.order_id = po.order_id
JOIN suppliers s
ON po.supplier_id = s.supplier_id
JOIN inventory i
ON poi.product_id = i.product_id;

-- Low Stock Products
SELECT *
FROM inventory
WHERE quantity <= minimum_stock;

-- Supplier Wise Products
SELECT
s.supplier_name,
i.product_name,
i.quantity
FROM suppliers s
JOIN inventory i
ON s.supplier_id=i.supplier_id;

-- Inventory Value
SELECT
SUM(quantity * purchase_price) AS Total_Inventory_Value
FROM inventory;