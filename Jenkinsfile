pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('sonar-credentials')
  }
  stages {



    stage('Build') {
      steps {
        echo 'Building Maven Project'
        sh 'mvn clean compile'
      }
    }


    stage('Unit Tests') {
      steps {
        echo 'Running Unit Tests with Coverage'
        sh 'mvn -Dtest=CourseServicesImplTest test jacoco:report'
      }
      post {
        always {
          junit '**/target/surefire-reports/TEST-*.xml'
          jacoco execPattern: '**/target/jacoco.exec'
        }
      }
    }

    stage('SonarQube Analysis') {
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
          withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
            sh """
              mvn deploy \
                -Dnexus.username=${NEXUS_USER} \
                -Dnexus.password=${NEXUS_PASS} \
                -DskipTests
            """
          }
        }
    }

    stage('Docker Image') {
      steps {
        sh 'docker build -t gestion-station-ski .'
      }
    }

    stage('Docker Compose') {
      steps {
        sh 'docker compose up -d'
      }
    }

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
