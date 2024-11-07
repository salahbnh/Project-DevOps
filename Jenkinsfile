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
        stage('SonarQube Analysis') {
                    steps {
                        withSonarQubeEnv('sonar') {
                           sh 'mvn sonar:sonar'
                        }
                    }
                }
    }
}