
BEGIN;

-- 1. Food items
INSERT INTO public.food_items
    (id, name, category, quantity, unit, expiry_date)
VALUES
    (1, 'Rice', 'Grains', 500.00, 'kg', NULL),
    (2, 'Wheat', 'Grains', 300.00, 'kg', NULL),
    (3, 'Milk', 'Dairy', 100.00, 'litres', '2026-10-15'),
    (4, 'Lentils', 'Pulses', 200.00, 'kg', NULL),
    (5, 'Cooking Oil', 'Oil', 75.00, 'litres', NULL);

-- 2. Beneficiaries
INSERT INTO public.beneficiaries
    (id, name, age, household_size, nutrition_needs, address)
SELECT
    n,
    'Beneficiary ' || LPAD(n::TEXT, 3, '0'),
    18 + (n % 60),
    1 + (n % 6),
    CASE
        WHEN n % 3 = 0 THEN 'High protein'
        WHEN n % 3 = 1 THEN 'Balanced nutrition'
        ELSE 'General nutrition'
    END,
    'Address ' || n
FROM generate_series(1, 500) AS n;

-- 3. Nutrition values (illustrative per 100 g or 100 ml)
INSERT INTO public.nutrition_values
    (id, food_item_id, calories, protein, carbohydrates, fat, vitamins, minerals)
VALUES
    (1, 1, 130, 2.7, 28.0, 0.3, 'B vitamins', 'Iron'),
    (2, 2, 340, 13.0, 72.0, 2.5, 'B vitamins', 'Iron'),
    (3, 3, 61, 3.2, 4.8, 3.3, 'Vitamin B12', 'Calcium'),
    (4, 4, 116, 9.0, 20.0, 0.4, 'B vitamins', 'Iron'),
    (5, 5, 884, 0.0, 0.0, 100.0, 'Vitamin E', 'None');

-- 4. Inventory batches
INSERT INTO public.inventory_batches
    (id, food_item_id, batch_number, quantity, unit, received_date, expiry_date)
VALUES
    (1, 1, 'BATCH-001', 500.00, 'kg', CURRENT_DATE, NULL),
    (2, 2, 'BATCH-002', 300.00, 'kg', CURRENT_DATE, NULL),
    (3, 3, 'BATCH-003', 100.00, 'litres', CURRENT_DATE, '2026-10-15'),
    (4, 4, 'BATCH-004', 200.00, 'kg', CURRENT_DATE, NULL),
    (5, 5, 'BATCH-005', 75.00, 'litres', CURRENT_DATE, NULL);

-- 5. Initial stock movements
INSERT INTO public.stock_movements
    (id, food_item_id, batch_id, movement_type, quantity, movement_date, remarks)
VALUES
    (1, 1, 1, 'IN', 500.00, CURRENT_DATE, 'Initial inventory setup'),
    (2, 2, 2, 'IN', 300.00, CURRENT_DATE, 'Initial inventory setup'),
    (3, 3, 3, 'IN', 100.00, CURRENT_DATE, 'Initial inventory setup'),
    (4, 4, 4, 'IN', 200.00, CURRENT_DATE, 'Initial inventory setup'),
    (5, 5, 5, 'IN', 75.00, CURRENT_DATE, 'Initial inventory setup');

-- 6. Example supplier
INSERT INTO public.suppliers
    (id, name, contact_person, phone, email, address)
VALUES
    (1, 'Sample Food Supplier', 'Supplier Contact',
     '9000000000', 'supplier@example.com', 'Sample Address');

-- 7. Example nutrition programme
INSERT INTO public.nutrition_programmes
    (id, name, description, start_date, end_date, status)
VALUES
    (1, 'Community Nutrition Programme',
     'Sample programme for demonstration',
     CURRENT_DATE, CURRENT_DATE + 30, 'ACTIVE');

-- 8. Example meal plan
INSERT INTO public.meal_plans
    (id, programme_id, name, meal_type, description, planned_date, notes)
VALUES
    (1, 1, 'Balanced Meal', 'Lunch',
     'Sample balanced meal plan', CURRENT_DATE + 1, 'Demonstration record');

-- 9. Programme participation
INSERT INTO public.programme_participation
    (id, programme_id, beneficiary_id, enrollment_date, status)
VALUES
    (1, 1, 1, CURRENT_DATE, 'ACTIVE');

-- 10. Example procurement
INSERT INTO public.procurements
    (id, food_item_id, quantity, procurement_date, supplier)
VALUES
    (1, 1, 50.00, CURRENT_DATE, 'Sample Food Supplier');

-- 11. Example distribution
INSERT INTO public.distributions
    (id, beneficiary_id, distribution_date, status)
VALUES
    (1, 1, CURRENT_DATE + 1, 'PLANNED');

-- 12. Items for that distribution
INSERT INTO public.distribution_items
    (id, distribution_id, food_item_id, quantity, unit)
VALUES
    (1, 1, 1, 1.00, 'kg');

-- 13. Example quality inspection
INSERT INTO public.quality_inspections
    (id, food_item_id, batch_id, inspection_date,
     inspector_name, quality_status, remarks)
VALUES
    (1, 1, 1, CURRENT_DATE, 'Sample Inspector',
     'PASS', 'Sample inspection record');

-- 14. Example wastage record
INSERT INTO public.wastage_records
    (id, food_item_id, batch_id, quantity, unit,
     wastage_reason, wastage_date, remarks)
VALUES
    (1, 4, 4, 1.00, 'kg',
     'Sample damage', CURRENT_DATE, 'Demonstration only');

COMMIT;