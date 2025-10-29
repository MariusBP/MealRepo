# MealRepo
A Spring Boot application for managing meals, ingredients, diets, and categories with a comprehensive REST API.

[![Java CI with Maven](https://github.com/MariusBP/MealRepo/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/MariusBP/MealRepo/actions/workflows/maven.yml)

## 🚀 Features

- **Meal Management**: Create, read, update, and delete meals with detailed nutritional information
- **Ingredient Database**: Comprehensive ingredient management with nutritional data
- **Diet Categories**: Support for various diet types (Vegetarian, Vegan, Pescatarian, Omnivore)
- **Category System**: Organize meals by categories (Breakfast, Lunch, Dinner, Snacks, etc.)
- **Allergy Management**: Track allergens and ingredient relationships
- **REST API**: Full OpenAPI 3.0 documented REST endpoints
- **Database Migrations**: Flyway-managed database schema and sample data

## 🛠️ Technology Stack

- **Java 21** with Spring Boot 3.5.0
- **PostgreSQL** database
- **Flyway** for database migrations
- **OpenAPI 3.0** for API documentation
- **Maven** for build management
- **Lombok** for reduced boilerplate code
- **MapStruct** for entity mapping

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 16
- Git

### Run Database Migrations
Use the provided script for easy database setup:

- mvn flyway:clean
- mvn flyway:migrate

## 📚 API Documentation

Once the application is running, you can access:
- **Swagger UI**: `http://localhost:8080/`

### Available Endpoints

| Endpoint | Description |
|----------|-------------|
| `/api/meals` | Meal management operations |
| `/api/diets` | Diet type management |
| `/api/categories` | Category management |
| `/api/ingredients` | Ingredient management |

## 🗄️ Database Schema

The application uses the following main entities:
- **Meals**: Core meal information with recipes and nutritional data
- **Ingredients**: Individual ingredients with nutritional values
- **Diets**: Diet classifications (Vegetarian, Vegan, etc.)
- **Categories**: Meal categories (Breakfast, Lunch, etc.)
- **Allergies**: Allergen information and ingredient relationships

## 🚀 CI/CD

The project includes GitHub Actions workflows for:
- **maven.yml**: Basic build and test pipeline
- **ci.yml**: Full CI/CD with database setup and Flyway migrations

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/mealapp/experiment/
│   │   ├── controller/     # REST Controllers
│   │   ├── service/        # Business Logic
│   │   ├── repository/     # Data Access Layer
│   │   ├── model/          # Entity Classes
│   │   └── config/         # Configuration Classes
│   └── resources/
│       ├── db/migration/   # Flyway SQL Scripts
│       ├── openapi/        # OpenAPI Specifications
│       └── application.properties
└── test/                   # Unit and Integration Tests
```

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- OpenAPI community for API documentation standards
- Flyway team for database migration tools
