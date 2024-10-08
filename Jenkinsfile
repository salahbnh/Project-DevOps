pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('jenkins-sonar') 
  }
  stages {
    stage('Checkout GIT') {
      steps {
        echo 'Pulling'
        git(
          branch: 'ahmed',
          url: 'https://github.com/salahbnh/Project-DevOps.git',
          credentialsId: 'ahmed'
        )
      }
    }
    
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