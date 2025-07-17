pipeline {
    agent any

    environment {
        // Путь к папке проекта на Open Server
        LOCAL_PATH = 'C:/OpenServer/domains/my-spring-app'
        // Имя JAR файла (должно совпадать с именем в pom.xml)
        JAR_NAME = 'my-spring-app.jar'
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
                    REM Удаляем старый JAR, если он есть
                    if exist \"${LOCAL_PATH}\\\\${JAR_NAME}\" del \"${LOCAL_PATH}\\\\${JAR_NAME}\"

                    REM Копируем новый JAR
                    copy \"target\\\\${JAR_NAME}\" \"${LOCAL_PATH}\"
                """
            }
        }

        stage('Run Application') {
            steps {
                bat """
                    @echo off
                    cd /d \"${LOCAL_PATH}\"

                    REM Останавливаем предыдущий процесс, если он запущен
                    tasklist | findstr :8080 >nul && (
                        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
                            taskkill /PID %%a /F
                        )
                    )

                    REM Запускаем приложение в фоне
                    start javaw -jar \"${JAR_NAME}\"
                """
            }
        }

        stage('Finish') {
            steps {
                echo '✅ sucsess'
            }
        }
    }
}