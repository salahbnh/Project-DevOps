pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('Jenkins-sonar')
    }
    stages {
//         stage('Build') {
//             steps {
//                 echo 'Building Maven Project'
//                 sh 'mvn clean package -DskipTests'
//             }
//         }
        stage('Mvn Test') {
            steps {
                echo 'Unit Tests'
                sh 'mvn test '
            }
        }
        stage('SonarQube Analysis') {
                    steps {
                        withSonarQubeEnv('sonar') {
                           sh 'mvn sonar:sonar'
                        }
                    }
                }
                stage('Build') {
                    steps {
                           sh 'mvn package'
                            }
                        }
                stage('Deploy to Nexus') {
                    steps {
                           withCredentials([usernamePassword(credentialsId: 'nexusToken', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
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
                                sh 'docker build -t gestion-station-ski .'
                              }
                            }

                            stage('Start Docker Compose') {
                              steps {
                                echo 'Starting Docker Compose for Integration Tests'
                                sh 'docker compose up -d'
                              }
                            }

    }
}