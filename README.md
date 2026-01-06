# Prism: Liquid Glass Media Management System

**Prism** is a full-stack media management application featuring a sophisticated Glassmorphism (Liquid Glass) UI. Built with a robust Spring Boot backend and an interactive Vue 3 frontend, Prism allows users to catalog, organize, and discover movies and music in a premium visual environment.

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Node.js (v18+)
- MySQL Server

### Running the Project
The project includes a root automation script `run.sh` that launches both the Backend (Spring Boot) and Frontend (Vue/Vite) simultaneously.

1. Configure your MySQL credentials in `Backend/src/main/resources/application.yml`.
2. Run the automation script:
   ```bash
   ./run.sh
   ```

## 📂 Project Structure

- **Backend/**: Spring Boot 3.4.x application (Java 17).
- **Frontend/vue-frontend/**: Vue 3 application with Tailwind CSS.
- **DOCS/**: Contains the ERD, Team Roles, Project Report, and Database Setup SQL.
- **Automation/**: Python scripts for data scraping and CSV cleaning.
- **data/**: CSV datasets for movies and music.

## 🛠️ Tech Stack

- **Backend**: Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate, MySQL.
- **Frontend**: Vue 3 (Composition API), Vite, Tailwind CSS, Axios, Pinia.
- **Documentation**: Swagger UI / OpenAPI 3.

## 📖 Documentation
Detailed project documentation, including the technical report and architectural diagrams, can be found in the `DOCS/` directory:
- [Technical Project Report](DOCS/PROJECT_REPORT.md)
- [Entity Relationship Diagram](DOCS/ERD.md)
- [Team Roles & Contributions](DOCS/TEAM_ROLES.md)

## 👤 Author
Developed by **Ebrima S Jallow** and team for the UTG Advanced Application Development course.
