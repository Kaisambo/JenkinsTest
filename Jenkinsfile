pipeline {
    agent any

    environment {
        RENDER_SERVICE_NAME = 'JenkinsTest' // замени на имя своего сервиса
        RENDER_API_KEY = credentials('render_api_key')
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

        stage('Debug') {
            steps {
                script {
                    echo "RENDER_API_KEY: ${RENDER_API_KEY}"
                    echo "RENDER_SERVICE_NAME: ${RENDER_SERVICE_NAME}"
                    echo "Git remote URL: https://${RENDER_API_KEY}@git.render.com/${RENDER_SERVICE_NAME}.git"
                }
            }
        }

      stage('Prepare for Render') {
          steps {
              script {
                  echo "Создаем временную папку для деплоя"
                  bat """
                      @echo on
                      REM Создаем папку, если её нет
                      if not exist \"render-deploy\" mkdir render-deploy

                      REM Удаляем старые файлы
                      if exist \"render-deploy\\app.jar\" del \"render-deploy\\app.jar\"

                      REM Копируем JAR файл
                      copy \"target\\PP3Task2-0.0.1-SNAPSHOT.jar\" \"render-deploy\\app.jar\"
                  """
              }
          }
      }

     stage('Deploy to Render') {
         steps {
             withCredentials([usernamePassword(
                 credentialsId: 'render_api_key',
                 passwordVariable: 'RENDER_API_KEY',
                 usernameVariable: 'RENDER_USER'
             )]) {
                 script {
                     echo "Формируем URL для Render"

                     bat """
                         cd /d render-deploy
                         git remote add origin https://${RENDER_API_KEY}@git.render.com/${RENDER_SERVICE_NAME}.git
                         git add .
                         git commit -m "Deploy from Jenkins"
                         git push origin HEAD:main

                     """
                 }
             }
         }
     }

        stage('Finish') {
            steps {
                echo "Deployed on: https://${RENDER_SERVICE_NAME}.onrender.com"
            }
        }
    }
}

