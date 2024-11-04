pipeline {
  agent any
  environment {
    SONAR_TOKEN = credentials('jenkins-sonar')
    DOCKER_REPO = 'docker.io/salahbnh1/devops-project'
    IMAGE_TAG = "${DOCKER_REPO}:${BUILD_NUMBER}" // Use Jenkins build number for unique tagging
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
        script {
          echo 'Checking if image already exists on Docker Hub...'
          def imageExists = sh(script: "docker pull ${IMAGE_TAG}", returnStatus: true) == 0

          if (!imageExists) {
            echo 'Building Docker Image'
            sh "docker build -t ${IMAGE_TAG} ."
          } else {
            echo 'Docker image already exists, skipping build.'
          }
        }
      }
    }

    stage('Push Docker Image') {
      when {
        expression {
          // Only push if the image was built in this pipeline run
          return !sh(script: "docker pull ${IMAGE_TAG}", returnStatus: true) == 0
        }
      }
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

    stage('Docker Run') {
      steps {
        echo 'Pulling Docker Image and Running Container'
        sh "docker pull ${IMAGE_TAG}"
        sh "docker run -d --name myapp-container -p 8089:8089 ${IMAGE_TAG}"
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
      // Optionally remove the local image to save space
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
