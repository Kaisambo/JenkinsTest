pipeline {
    agent any

    environment {
        RENDER_SERVICE_NAME = 'your-render-service-name' // замени на имя своего сервиса
        RENDER_API_KEY = credentials('render-api-key') // ID креда в Jenkins
        GIT_REPO_URL = 'https://github.com/ ваше_имя/ваш_репо.git'
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
                    echo "Создаем архив проекта"
                    sh '''
                        mkdir -p build-output
                        cp target/*.jar build-output/app.jar
                        cp Procfile build-output || echo "Procfile не найден"
                        cp pom.xml build-output
                        cd build-output
                        git init
                        git remote add origin https://github.com/ ваше_имя/ваш_репо.git
                        sh '''
                            echo "web: java -jar target/PP3Task2-0.0.1-SNAPSHOT.jar" > Procfile
                        '''
                        git add .
                        git commit -m "Deploying from Jenkins"
                    '''
                }
            }
        }

        stage('Deploy to Render') {
            steps {
                script {
                    echo "Push на Render через Git"
                    sh '''
                        cd build-output
                        git remote set-url origin https://${RENDER_API_KEY}@git.render.com/${RENDER_SERVICE_NAME}.git
                        git push origin HEAD:main --force
                    '''
                }
            }
        }

        stage('Finish') {
            steps {
                echo "Приложение задеплоено по адресу: https://${RENDER_SERVICE_NAME}.onrender.com"
            }
        }
    }
}