pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('jenkins-sonar')
    NEXUS_USER = credentials('nexus-credentials').username
    NEXUS_PASS = credentials('nexus-credentials').password
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
