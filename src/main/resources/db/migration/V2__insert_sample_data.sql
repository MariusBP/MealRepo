-- Sample data insertion for diet, category, allergy, ingredient, meal, ingredient_meal, category_meal, and allergy_ingredient tables

INSERT INTO diet (name, description, picture) VALUES
('Vegetarian', 'A diet that excludes meat, poultry, and fish', 'vegetarian.jpg'),
('Vegan', 'A diet that excludes all animal products', 'vegan.jpg'),
('Pescatarian', 'A diet that excludes meat and poultry but includes fish and seafood', 'pescatarian.jpg'),
('Omnivore', 'A diet that includes both plant and animal foods', 'omnivore.jpg');

INSERT INTO category (name, description) VALUES
('Breakfast', 'Morning meals'),
('Lunch', 'Midday meals'),
('Dinner', 'Evening meals'),
('Snack', 'Light meals between main meals'),
('Dessert', 'Sweet treats and after-meal foods'),
('Appetizer', 'Small dishes served before the main course'),
('Soup', 'Liquid-based dishes'),
('Salad', 'Fresh vegetable-based dishes'),
('Keto', 'A high-fat, low-carbohydrate diet'),
('Mediterranean', 'A diet based on the traditional eating patterns of Mediterranean countries'),
('Paleo', 'A diet based on foods similar to what might have been eaten during the Paleolithic era');

INSERT INTO allergy (name) VALUES
('Gluten'),
('Dairy'),
('Nuts'),
('Shellfish'),
('Eggs'),
('Soy'),
('Fish'),
('Sesame');

INSERT INTO ingredient (name, kcal, carb, fat, protein, fiber, sodium) VALUES
('Chicken Breast', 165.0, 0.0, 3.6, 31.0, 0.0, 74.0),
('Brown Rice', 123.0, 23.0, 0.9, 2.6, 1.8, 5.0),
('Broccoli', 34.0, 7.0, 0.4, 2.8, 2.6, 33.0),
('Olive Oil', 884.0, 0.0, 100.0, 0.0, 0.0, 2.0),
('Tomato', 18.0, 3.9, 0.2, 0.9, 1.2, 5.0),
('Onion', 40.0, 9.3, 0.1, 1.1, 1.7, 4.0),
('Garlic', 149.0, 33.1, 0.5, 6.4, 2.1, 17.0),
('Spinach', 23.0, 3.6, 0.4, 2.9, 2.2, 79.0),
('Salmon', 208.0, 0.0, 12.4, 22.1, 0.0, 59.0),
('Quinoa', 368.0, 64.2, 6.1, 14.1, 7.0, 5.0),
('Avocado', 160.0, 8.5, 14.7, 2.0, 6.7, 7.0),
('Bell Pepper', 31.0, 7.3, 0.3, 1.0, 2.5, 4.0),
('Mushrooms', 22.0, 3.3, 0.3, 3.1, 1.0, 5.0),
('Sweet Potato', 86.0, 20.1, 0.1, 1.6, 3.0, 54.0),
('Greek Yogurt', 59.0, 3.6, 0.4, 10.0, 0.0, 36.0);

INSERT INTO meal (name, description, recipe, prep_time, calories, servings, diet_id) VALUES
('Grilled Chicken with Vegetables',
 'A healthy and protein-rich meal with grilled chicken breast and fresh vegetables',
 '1. Season chicken breast with salt and pepper. 2. Grill for 6-8 minutes per side. 3. Steam broccoli for 5 minutes. 4. Sauté bell peppers with olive oil. 5. Serve together.',
 25, 350, 2, 4),

('Quinoa Buddha Bowl',
 'A nutritious vegetarian bowl with quinoa, avocado, and fresh vegetables',
 '1. Cook quinoa according to package instructions. 2. Slice avocado and tomatoes. 3. Steam broccoli. 4. Arrange in bowl and drizzle with olive oil.',
 20, 420, 1, 1),

('Salmon with Sweet Potato',
 'Baked salmon fillet with roasted sweet potato and spinach',
 '1. Preheat oven to 400°F. 2. Season salmon with herbs. 3. Roast sweet potato for 25 minutes. 4. Bake salmon for 12-15 minutes. 5. Sauté spinach with garlic.',
 35, 480, 2, 4),

('Mediterranean Vegetable Salad',
 'Fresh salad with tomatoes, bell peppers, and olive oil dressing',
 '1. Chop tomatoes, bell peppers, and onions. 2. Mix with olive oil and herbs. 3. Season with salt and pepper. 4. Let marinate for 10 minutes.',
 15, 180, 4, 4),

('Vegan Mushroom Stir-fry',
 'Quick and easy stir-fry with mushrooms, spinach, and garlic',
 '1. Heat oil in pan. 2. Sauté garlic and onions. 3. Add mushrooms and cook until tender. 4. Add spinach and cook until wilted. 5. Season and serve.',
 15, 120, 2, 2);

INSERT INTO ingredient_meal (meal_id, ingredient_id, amount, unit) VALUES
(1, 1, 150.0, 'g'),
(1, 3, 100.0, 'g'),
(1, 12, 80.0, 'g'),
(1, 4, 10.0, 'ml');

INSERT INTO ingredient_meal (meal_id, ingredient_id, amount, unit) VALUES
(2, 10, 80.0, 'g'),
(2, 11, 100.0, 'g'),
(2, 5, 80.0, 'g'),
(2, 3, 60.0, 'g'),
(2, 4, 15.0, 'ml');

INSERT INTO ingredient_meal (meal_id, ingredient_id, amount, unit) VALUES
(3, 9, 120.0, 'g'),
(3, 14, 150.0, 'g'),
(3, 8, 80.0, 'g'),
(3, 7, 2.0, 'cloves');

INSERT INTO ingredient_meal (meal_id, ingredient_id, amount, unit) VALUES
(4, 5, 200.0, 'g'),
(4, 12, 100.0, 'g'),
(4, 6, 50.0, 'g'),
(4, 4, 20.0, 'ml');

INSERT INTO ingredient_meal (meal_id, ingredient_id, amount, unit) VALUES
(5, 13, 150.0, 'g'),
(5, 8, 100.0, 'g'),
(5, 7, 3.0, 'cloves'),
(5, 6, 40.0, 'g'),
(5, 4, 10.0, 'ml');

INSERT INTO category_meal (meal_id, category_id) VALUES
(1, 3),
(1, 2),
(2, 2),
(2, 7),
(3, 3),
(4, 7),
(4, 6),
(5, 2),
(5, 3);

INSERT INTO allergy_ingredient (ingredient_id, allergy_id) VALUES
(1, 5),
(9, 7),
(15, 2),
(10, 1);
