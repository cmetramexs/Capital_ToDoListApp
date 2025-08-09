# Project Structure
task-management-app/
├── frontend/                 # React frontend application
│   ├── public/              # Static assets
│   ├── src/
│   │   ├── components/      # Reusable components
│   │   │   ├── ui/         # Basic UI components
│   │   │   ├── forms/      # Form components
│   │   │   └── layout/     # Layout components
│   │   ├── pages/          # Page components
│   │   │   ├── tasks/      # Task-related pages
│   │   │   └── auth/       # Authentication pages
│   │   ├── hooks/          # Custom React hooks
│   │   ├── services/       # API services
│   │   ├── context/        # React context providers
│   │   ├── types/          # TypeScript type definitions
│   │   ├── utils/          # Utility functions
│   │   └── assets/         # Images, icons, etc.
│   ├── package.json
│   └── .gitignore
├── backend/                 # Spring Boot backend application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/taskmanagement/backend/
│   │   │   │   ├── config/        # Configuration classes
│   │   │   │   ├── controller/    # REST controllers
│   │   │   │   ├── dto/          # Data Transfer Objects
│   │   │   │   ├── entity/       # JPA entities
│   │   │   │   ├── exception/    # Exception handling
│   │   │   │   ├── repository/   # Data repositories
│   │   │   │   ├── security/     # Security configuration
│   │   │   │   ├── service/      # Business logic
│   │   │   │   └── util/         # Utility classes
│   │   │   └── resources/        # Configuration files
│   │   └── test/                 # Test classes
│   ├── pom.xml
│   └── .gitignore
├── docs/                    # Documentation
├── scripts/                # Build and deployment scripts
├── docker/                 # Docker configurations
├── docker-compose.dev.yml  # Development environment
├── package.json            # Root package.json for scripts
├── PROJECT_STRUCTURE.md    # This file
├── README.md               # Main project documentation
└── .gitignore              # Root gitignore
