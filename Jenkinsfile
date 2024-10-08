pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Building Maven Project'
        sh 'mvn compile'
      }
    }
   // stage('Mvn SonarQube') {
    //  steps {
     //   echo 'Static Analysis'
      //  sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
     // }
   // }
  }
}