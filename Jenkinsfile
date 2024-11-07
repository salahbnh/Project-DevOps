pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('jenkins-sonar')
  }
  stages {
    stage('Checkout GIT') {
      steps {
        echo 'Pulling from Git repository'
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

   // stage('Generate JaCoCo Report') {
      //  steps {
      //    echo 'Preparing JaCoCo Agent and Generating Report'
      //    sh 'mvn clean verify jacoco:report'
     //   }
   //   }
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
