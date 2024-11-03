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

    stage('Unit Tests') {
      steps {
        echo 'Running Unit Tests with Coverage'
        sh 'mvn -Dtest=SkierServicesImplTest test jacoco:report'
      }
      post {
        always {
          junit '**/target/surefire-reports/TEST-*.xml'
          jacoco execPattern: '**/target/jacoco.exec'
        }
      }
    }

    stage('Docker Build') {
      steps {
        echo 'Building Docker Image'
        sh 'docker build -t myapp:latest .'
      }
    }

    stage('Docker Compose') {
      steps {
        echo 'Running Docker Compose'
        sh 'docker compose up -d'
        sh 'docker compose ps'
      }
    }

    stage('Integration Tests') {
      steps {
        echo 'Running Integration Tests on Spring Boot container'
        sh """
          docker exec app mvn -Dtest=SkierServiceIntegrationTest test
        """
      }
      post {
        always {
          // Collect test reports
          junit '**/target/surefire-reports/TEST-*.xml'
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

  post {
    always {
      echo 'Cleaning up Docker containers'
      sh 'docker-compose down'
    }
  }
}
