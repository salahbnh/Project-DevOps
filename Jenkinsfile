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
          sh 'mvn test'
        }
        post {
            always {
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
    }

    stage('Mvn SonarQube') {
      steps {
        echo 'Static Analysis'
        sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
      }
    }

     stage('Deploy To Nexus') {
          steps {
            echo 'Deploiment to Nexus'
            sh 'mvn deploy -Dnexus.login=<admin> -Dnexus.password=<nexus>'
          }
        }
  }
}
