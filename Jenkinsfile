pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('jenkins-sonar')
    DOCKER_REPO = 'devops-project'
    CONTAINER_NAME = "devops-project-container"
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

    stage('Build Docker Image') {
      steps {
        echo 'Building Docker Image'
        sh "docker build -t ${DOCKER_REPO} ."
      }
    }

    stage('Run Docker Compose') {
      steps {
        echo 'Starting Services with Docker Compose'
        sh 'docker-compose down || true'  // Stop any previous instances
        sh 'docker-compose up -d --build'
      }
    }
  }

//   post {
//     always {
//       echo 'Cleaning up Docker containers and images'
//       sh "docker stop ${CONTAINER_NAME} || true"
//       sh "docker rm ${CONTAINER_NAME} || true"
//       sh "docker rmi ${DOCKER_REPO} || true"
//     }
//   }
}



//     stage('Integration Tests') {
//       steps {
//         echo 'Running Integration Tests on Spring Boot container'
//         sh """
//           docker exec app mvn -Dtest=SkierServiceIntegrationTest test
//         """
//       }
//       post {
//         always {
//           // Collect test reports
//           junit '**/target/surefire-reports/TEST-*.xml'
//         }
//       }
//     }
