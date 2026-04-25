-- Seed categories (idempotent: only inserts if missing)
INSERT INTO categories (name, description, image_url, created_at)
SELECT * FROM (SELECT 'Electronics' AS name, 'Phones, laptops, audio and gadgets' AS description,
                      'https://picsum.photos/seed/electronics/600/400' AS image_url, NOW() AS created_at) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Electronics');

INSERT INTO categories (name, description, image_url, created_at)
SELECT * FROM (SELECT 'Apparel', 'Clothing, footwear and accessories',
                      'https://picsum.photos/seed/apparel/600/400', NOW()) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Apparel');

INSERT INTO categories (name, description, image_url, created_at)
SELECT * FROM (SELECT 'Home & Kitchen', 'Cookware, appliances and decor',
                      'https://picsum.photos/seed/home/600/400', NOW()) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Home & Kitchen');

-- Seed products (only when table is empty so re-runs don't duplicate)
INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Wireless Headphones', 'Over-ear noise-cancelling Bluetooth headphones with 30h battery.',
       2999.00, 10.00, 50, 'https://picsum.photos/seed/headphones/400/400',
       (SELECT id FROM categories WHERE name = 'Electronics'), TRUE, TRUE, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM products LIMIT 1);

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT '14-inch Laptop', 'Slim productivity laptop, 16GB RAM, 512GB SSD.',
       54990.00, 5.00, 12, 'https://picsum.photos/seed/laptop/400/400',
       (SELECT id FROM categories WHERE name = 'Electronics'), TRUE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 2;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Smart Watch', 'AMOLED display, heart rate, GPS, 7-day battery.',
       7499.00, 15.00, 30, 'https://picsum.photos/seed/watch/400/400',
       (SELECT id FROM categories WHERE name = 'Electronics'), FALSE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 3;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Bluetooth Speaker', 'Compact waterproof speaker with 12h playback.',
       1999.00, 0.00, 80, 'https://picsum.photos/seed/speaker/400/400',
       (SELECT id FROM categories WHERE name = 'Electronics'), FALSE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 4;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT '4K Action Camera', 'Compact action camera with stabilization.',
       8999.00, 8.00, 20, 'https://picsum.photos/seed/camera/400/400',
       (SELECT id FROM categories WHERE name = 'Electronics'), FALSE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 5;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Cotton T-Shirt', 'Soft 100% cotton crew-neck.',
       599.00, 20.00, 200, 'https://picsum.photos/seed/tshirt/400/400',
       (SELECT id FROM categories WHERE name = 'Apparel'), TRUE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 6;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Slim-fit Jeans', 'Stretch denim, mid-rise.',
       1499.00, 0.00, 120, 'https://picsum.photos/seed/jeans/400/400',
       (SELECT id FROM categories WHERE name = 'Apparel'), FALSE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 7;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Running Shoes', 'Lightweight cushioned daily trainers.',
       3299.00, 10.00, 75, 'https://picsum.photos/seed/shoes/400/400',
       (SELECT id FROM categories WHERE name = 'Apparel'), TRUE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 8;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Leather Wallet', 'Bifold genuine leather wallet.',
       899.00, 0.00, 90, 'https://picsum.photos/seed/wallet/400/400',
       (SELECT id FROM categories WHERE name = 'Apparel'), FALSE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 9;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Backpack 25L', 'Water-resistant everyday backpack.',
       1799.00, 0.00, 60, 'https://picsum.photos/seed/backpack/400/400',
       (SELECT id FROM categories WHERE name = 'Apparel'), FALSE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 10;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Non-stick Frying Pan', '24cm non-stick pan, induction-friendly.',
       1099.00, 0.00, 40, 'https://picsum.photos/seed/pan/400/400',
       (SELECT id FROM categories WHERE name = 'Home & Kitchen'), FALSE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 11;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Electric Kettle 1.5L', 'Auto cut-off, stainless steel interior.',
       1399.00, 5.00, 35, 'https://picsum.photos/seed/kettle/400/400',
       (SELECT id FROM categories WHERE name = 'Home & Kitchen'), TRUE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 12;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Coffee Maker', '12-cup drip coffee maker with timer.',
       3299.00, 12.00, 18, 'https://picsum.photos/seed/coffee/400/400',
       (SELECT id FROM categories WHERE name = 'Home & Kitchen'), FALSE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 13;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'Vacuum Cleaner', 'Bagless cyclonic with HEPA filter.',
       6499.00, 8.00, 15, 'https://picsum.photos/seed/vacuum/400/400',
       (SELECT id FROM categories WHERE name = 'Home & Kitchen'), FALSE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 14;

INSERT INTO products (name, description, price, discount_pct, stock_qty, image_url, category_id, is_featured, is_active, created_at, updated_at)
SELECT 'LED Desk Lamp', 'Dimmable touch-control desk lamp with USB port.',
       1299.00, 0.00, 70, 'https://picsum.photos/seed/lamp/400/400',
       (SELECT id FROM categories WHERE name = 'Home & Kitchen'), FALSE, TRUE, NOW(), NOW()
FROM dual WHERE (SELECT COUNT(*) FROM products) < 15;
