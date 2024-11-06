pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('Jenkins-sonar')
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building Maven Project'
                sh 'mvn clean compile'
            }
        }
        stage('Mvn Test') {
            steps {
                echo 'Unit Tests'
                sh 'mvn clean test jacoco:report'
            }
            post {
                always {
                    // Temporarily disable if not needed:
                    // junit '**/target/surefire-reports/TEST-*.xml'
                    jacoco execPattern: '**/target/jacoco.exec'
                }
            }
        }
        stage('Mvn SonarQube') {
            steps {
                echo 'Static Analysis'
                sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
            }
        }
    }
}
