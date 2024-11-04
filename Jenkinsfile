pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('jenkins-sonar')
    DOCKER_REPO = 'docker.io/salahbnh1/devops-project'
    IMAGE_TAG = "${DOCKER_REPO}:${BUILD_NUMBER}" // Use Jenkins build number for unique tagging
    CONTAINER_NAME = "devops-project-container"
  }

  options {
    timeout(time: 20, unit: 'MINUTES')  // Set a timeout for the entire pipeline
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

    stage('Build Docker Image') {
      steps {
        script {
          echo 'Building Docker Image'
          // Pull the latest base images first to reduce build time if cached
          sh "docker pull openjdk:17-jdk-slim || true"
          sh "docker pull maven:3.8.5-openjdk-17 || true"
          // Build the image
          sh "docker build -t ${IMAGE_TAG} ."
        }
      }
    }

    stage('Push Docker Image') {
      steps {
        script {
          echo 'Pushing Docker Image to Docker Hub'
          withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh "echo $DOCKER_PASS | docker login docker.io -u $DOCKER_USER --password-stdin"
          }
          sh "docker push ${IMAGE_TAG}"
        }
      }
    }

    stage('Run Docker Container') {
      steps {
        script {
          echo 'Running Docker Container'
          // Stop and remove any existing container with the same name to avoid conflicts
          sh "docker stop ${CONTAINER_NAME} || true && docker rm ${CONTAINER_NAME} || true"

          // Pull the latest pushed image
          sh "docker pull ${IMAGE_TAG}"
          // Run the container
          sh "docker run -d --name ${CONTAINER_NAME} -p 8089:8089 ${IMAGE_TAG}"
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
      echo 'Cleaning up Docker containers and images'
      // Stop and remove the running container after the build
      sh "docker stop ${CONTAINER_NAME} || true"
      sh "docker rm ${CONTAINER_NAME} || true"
      // Remove the built image to free up space
      sh "docker rmi ${IMAGE_TAG} || true"
    }
  }
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
