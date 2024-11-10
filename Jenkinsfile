pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('sonar-credentials')
  }
  stages {



    stage('Build') {
      steps {
        echo 'Building Maven Project'
        sh 'mvn clean compile'
      }
    }

    stage('Static Analysis - SonarQube') {
      steps {
        echo 'Running SonarQube Analysis'
        sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
      }
    }

    stage('Unit Tests') {
      steps {
        echo 'Running Unit Tests'
        sh 'mvn test'
      }
    }

    stage('Deploy To Nexus') {
          steps {
            echo 'Deploying to Nexus'
            withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
              sh """
                mvn deploy \
                  -Dnexus.username=${NEXUS_USER} \
                  -Dnexus.password=${NEXUS_PASS} \
                  -DskipTests
              """
            }
          }
        }
        stage('Docker Image') {
          steps {
            sh 'docker build -t gestion-station-ski .'
          }
        }

        stage('Docker Compose') {
          steps {
            sh 'docker compose up -d'
          }
        }

  }

  post {
    always {
      echo 'Cleaning up Workspace'
      cleanWs()
    }
    success {
      echo 'Build Completed Successfully'
    }
    failure {
      echo 'Build Failed'
    }
  }
}
