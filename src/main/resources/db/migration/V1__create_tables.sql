-- Create tables for meal application

CREATE TABLE diet (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    picture TEXT,
    description VARCHAR(255)
);

CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE allergy (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE ingredient (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    kcal DOUBLE PRECISION NOT NULL,
    carb DOUBLE PRECISION NOT NULL,
    fat DOUBLE PRECISION NOT NULL,
    protein DOUBLE PRECISION NOT NULL,
    fiber DOUBLE PRECISION NOT NULL,
    sodium DOUBLE PRECISION NOT NULL
);

-- Create tables with foreign key dependencies
CREATE TABLE meal (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    picture TEXT,
    description VARCHAR(255),
    recipe TEXT,
    prep_time INTEGER,
    calories INTEGER DEFAULT 0 NOT NULL,
    servings INTEGER DEFAULT 4 NOT NULL,
    diet_id BIGINT NOT NULL,
    date_created DATE DEFAULT CURRENT_DATE NOT NULL,
    CONSTRAINT fk_meal_diet FOREIGN KEY (diet_id) REFERENCES diet(id)
);

-- many-to-many relationship tables
CREATE TABLE ingredient_meal (
    id BIGSERIAL PRIMARY KEY,
    meal_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    unit VARCHAR(50) NOT NULL,
    CONSTRAINT fk_ingredient_meal_meal FOREIGN KEY (meal_id) REFERENCES meal(id) ON DELETE CASCADE,
    CONSTRAINT fk_ingredient_meal_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);

CREATE TABLE category_meal (
    category_id BIGINT NOT NULL,
    meal_id BIGINT NOT NULL,
    PRIMARY KEY (meal_id, category_id),
    CONSTRAINT fk_category_meal_meal FOREIGN KEY (meal_id) REFERENCES meal(id) ON DELETE CASCADE,
    CONSTRAINT fk_category_meal_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

CREATE TABLE allergy_ingredient (
    allergy_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    PRIMARY KEY (ingredient_id, allergy_id),
    CONSTRAINT fk_allergy_ingredient_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE,
    CONSTRAINT fk_allergy_ingredient_allergy FOREIGN KEY (allergy_id) REFERENCES allergy(id) ON DELETE CASCADE
);

-- Indexes for better performance
CREATE INDEX idx_meal_diet_id ON meal(diet_id);
CREATE INDEX idx_meal_date_created ON meal(date_created);
CREATE INDEX idx_meal_name ON meal(name);

CREATE INDEX idx_ingredient_meal_meal_id ON ingredient_meal(meal_id);
CREATE INDEX idx_ingredient_meal_ingredient_id ON ingredient_meal(ingredient_id);

CREATE INDEX idx_ingredient_name ON ingredient(name);
CREATE INDEX idx_category_name ON category(name);
CREATE INDEX idx_diet_name ON diet(name);
CREATE INDEX idx_allergy_name ON allergy(name);
