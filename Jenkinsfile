pipeline {
  agent any
  stages {
    stage('Checkout GIT') {
      steps {
        sh 'echo"Pulling"'
        git branch: 'salah', 
            url: 'https://github.com/salahbnh/Project-DevOps.git'
            credentialsId:'jenkins-salah', 
      }
    }
  }
}