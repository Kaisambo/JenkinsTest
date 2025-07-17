pipeline {
    agent any

    environment {
        RENDER_SERVICE_NAME = 'jenkinsTest' // имя сервиса на Render
        RENDER_CRED_ID = 'render_api_key' // ID креда в Jenkins
        RENDER_API_KEY = credentials('render_api_key')
    }

    stages {
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

        stage('Prepare for Render') {
            steps {
                script {
                    echo "Создаем временную папку render-deploy"
                    bat """
                        @echo on

                        REM Удаляем старую папку, если есть
                        if exist \"render-deploy\" rmdir /s /q \"render-deploy\"

                        REM Создаем новую
                        mkdir render-deploy

                        REM Копируем .jar
                        copy \"target\\PP3Task2-0.0.1-SNAPSHOT.jar\" \"render-deploy\\app.jar\"

                        REM Создаем Procfile
                        echo web: java -jar app.jar > \"render-deploy\\Procfile\"
                    """
                }
            }
        }


        stage('Deploy to Render') {
            steps
                {
                    script {
                        echo "Пушим на Render: ${RENDER_SERVICE_NAME}"

                        bat """
                            @echo on
                            cd /d \"render-deploy\"

                            REM Инициализируем Git
                            git init
                            git config --local user.email \"jenkins@example.com\"
                            git config --local user.name \"Jenkins\"

                            REM Удаляем старый remote
                            git remote remove origin || echo Origin not found

                            REM Добавляем новый remote
                             git remote add origin https://${RENDER_API_KEY}@git.render.com/${RENDER_SERVICE_NAME}.git

                            REM Добавляем файлы и делаем push
                            git add .
                            git commit -m \"Deploy .jar to Render\"
                            git push origin HEAD:main --force
                        """
                    }
                }
            }
        }

        stage('Finish') {
            steps {
                echo "✅ Приложение задеплоено на Render"
                echo "Открой: https://${RENDER_SERVICE_NAME}.onrender.com"
            }
        }
    }
}