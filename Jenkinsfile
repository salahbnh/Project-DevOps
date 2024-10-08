pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('jenkins-sonar') // Make sure to set this in Jenkins Credentials
  }
  stages {
    // stage('Checkout GIT') {
    //   steps {
    //     echo 'Pulling'
    //     git(
    //       branch: 'salah',
    //       url: 'https://github.com/salahbnh/Project-DevOps.git',
    //       credentialsId: 'jenkins-salah'
    //     )
    //   }
    // }
    
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
