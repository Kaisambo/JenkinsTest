pipeline {
    agent any

    environment {
        // Path to project folder on Open Server
        LOCAL_PATH = 'C:/OpenServer/domains/jenkinsproj'
        // JAR file name (must match name in pom.xml)
        JAR_NAME = 'PP3Task2-0.0.1-SNAPSHOT.jar'
    }

    stages {
        // 1. Get code from GitHub
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }



        // 3. Copy JAR file to Open Server directory
        stage('Copy to Open Server') {
            steps {
               bat """
                   @echo on

                   REM Удаление старого JAR файла
                   if exist \"${LOCAL_PATH}\\\\${JAR_NAME}\" del /Q \"${LOCAL_PATH}\\\\${JAR_NAME}\"

                   REM Копирование нового JAR
                   copy \"target\\\\${JAR_NAME}\" \"${LOCAL_PATH}\"
               """
            }
        }

        stage('Run Application') {
            steps {
                bat """
                    @echo on
                    cd /d \"${LOCAL_PATH}\"

                    REM Stop previous process if running
                    tasklist | findstr :8080 >nul && (
                        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
                            echo Killing process with PID: %%a
                            taskkill /PID %%a /F
                        )
                    )

                    REM Start new JAR application in background
                    start "" javaw -jar "PP3Task2-0.0.1-SNAPSHOT.jar"
                """
            }
        }

        stage('Finish') {
            steps {
                echo '✅ Application successfully deployed on Open Server Panel'
            }
        }
    }
}
