pipeline {
    agent any

    environment {
        // Путь к папке проекта на Open Server
        LOCAL_PATH = 'C:/OpenServer/domains/jenkinsproj'
        // Имя JAR файла (должно совпадать с именем в pom.xml)
        JAR_NAME = 'PP3Task2-0.0.1-SNAPSHOT.jar'
    }

    stages {
        // 1. Получение кода из GitHub
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

        // 3. Копирование JAR файла в папку Open Server
        stage('Copy to Open Server') {
            steps {
                bat """
                    @echo off
                    REM delete old jar
                    if exist \"${LOCAL_PATH}\\\\${JAR_NAME}\" del \"${LOCAL_PATH}\\\\${JAR_NAME}\"

                    REM copy new jar
                    copy \"target\\\\${JAR_NAME}\" \"${LOCAL_PATH}\"
                """
            }
        }

        stage('Run Application') {
            steps {
                bat """
                    @echo off
                    cd /d \"${LOCAL_PATH}\"

                    REM Оstop pred process
                    tasklist | findstr :8080 >nul && (
                        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
                            taskkill /PID %%a /F
                        )
                    )

                    REM start app
                    start javaw -jar \"${JAR_NAME}\"
                """
            }
        }

        stage('Finish') {
            steps {
                echo 'sucsess'
            }
        }
    }
}