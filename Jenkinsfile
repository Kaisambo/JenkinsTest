pipeline {
    agent any

    environment {
        RENDER_SERVICE_NAME = 'JenkinsTest' // замени на имя своего сервиса
        RENDER_API_KEY = credentials('render-api-ley') // ID креда в Jenkins
        GIT_REPO_URL = 'https://github.com/Kaisambo/JenkinsTest.git'
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
                        git remote add origin https://github.com/Kaisambo/JenkinsTest.git
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

"rnd_QUIad422BGAdmqgvWI4C54uvLDQX - рендер апи ключ"