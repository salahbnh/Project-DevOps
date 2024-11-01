pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('jenkins-sonar')
  }
  stages {
    stage('Build') {
      steps {
        echo 'Building Maven Project'
        sh 'mvn compile'
      }
    }

    stage('Mvn Test') {
        steps {
          echo 'Unit Tests'
          sh 'mvn test jacoco:report'
        }
        post {
            always {
                junit '**/target/surefire-reports/TEST-*.xml'
                jacoco execPattern: '**/target/jacoco.exec'
            }
        }
    }

    stage('Mvn SonarQube') {
      steps {
       echo 'Static Analysis with SonarQube'
       sh """
         mvn sonar:sonar \
           -Dsonar.login=${SONAR_TOKEN} \
           -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
       """
      }
    }

    stage('Docker Build') {
          steps {
            echo 'Building Docker Image'
            sh 'docker build -t myapp:latest .'
          }
    }

    stage('Docker Compose') {
      steps {
        echo 'Running Docker Compose'
        sh 'docker-compose up -d'
      }
    }

    stage('Deploy To Nexus') {
      steps {
        echo 'Deploying to Nexus'
         withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
             sh """
                 mvn deploy \
                 -Dnexus.username=${NEXUS_USER} \
                 -Dnexus.password=${NEXUS_PASS} \
                 -DskipTests
             """
         }
      }
    }
  }
}
