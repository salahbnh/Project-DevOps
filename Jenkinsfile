pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('Jenkins-sonar')
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building Maven Project'
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Mvn Test') {
            steps {
                echo 'Unit Tests'
                sh 'mvn test '
            }
        }
        stage('Mvn SonarQube') {
            steps {
                echo 'Static Analysis'
                sh "mvn sonar:sonar -Dsonar.login=${Jenkins}"
            }
        }
    }
}
