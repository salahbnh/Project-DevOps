pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('Jenkins-sonar') // Make sure to set this in Jenkins Credentials
  }
  stages {
    stage('Build') {
      steps {
        echo 'Building Maven Project'
        sh 'mvn compile'
      }
    }
    stage('Mvn SonarQube') {
      steps {
        echo 'Static Analysis'
        sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
      }
    }
  }
}