pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('jenkins-sonar')
    DOCKER_REPO = 'docker.io/salahbnh1/devops-project'
    IMAGE_TAG = "${DOCKER_REPO}:${BUILD_NUMBER}" // Use Jenkins build number for unique tagging
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

    stage('Build Docker Image') {
      steps {
        script {
          echo 'Building Docker Image'
          sh "docker build -t ${IMAGE_TAG} ."
        }
      }
    }

    stage('Push Docker Image') {
      steps {
        script {
          echo 'Pushing Docker Image to Docker Hub'
          withCredentials([usernamePassword(credentialsId: 'docker-jenkins', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
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
          sh "docker stop ${CONTAINER_NAME} || true && docker rm ${CONTAINER_NAME} || true"
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
      echo 'Cleaning up Docker containers'
      sh 'docker stop myapp-container || true'
      sh 'docker rm myapp-container || true'
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
