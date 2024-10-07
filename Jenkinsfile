pipeline {
  agent any
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
  }
}