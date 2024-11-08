pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('jenkins-sonar')
  }

  stages {

    stage('Build') {
      steps {
        echo 'Building Maven Project'
        sh 'mvn clean package -DskipTests'
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

    stage('Build Docker Image') {
      steps {
        echo 'Building Docker Image'
        sh 'docker build -t gestion-station-ski .'
      }
    }

    stage('Start Docker Compose') {
      steps {
        echo 'Starting Docker Compose for Integration Tests'
        sh 'docker compose up -d'
      }
    }

    stage('Run Integration Tests') {
      steps {
        echo 'Running Integration Tests in Docker Container'
        // Run the tests inside the app container
        sh 'docker compose exec app mvn -Dtest=SkierServiceIntegrationTest test jacoco:report'
      }
      post {
        always {
          junit '**/target/surefire-reports/TEST-*.xml'
          jacoco execPattern: '**/target/jacoco.exec'
        }
      }
    }

    stage('SonarQube Analysis for Integration Tests') {
      steps {
        echo 'Static Analysis with SonarQube for Integration Tests'
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
    success {
        emailext (
            subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: "The build completed successfully. Check the details at ${env.BUILD_URL}.",
            recipientProviders: [[$class: 'DevelopersRecipientProvider']]
        )
    }
    failure {
        emailext (
            subject: "FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: "The build failed. Check the details at ${env.BUILD_URL}.",
            recipientProviders: [[$class: 'DevelopersRecipientProvider']]
        )
    }
 }
}
