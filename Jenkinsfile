pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('Jenkins-sonar')  // Securely use SonarQube token from Jenkins credentials store
    }
    stages {
        // Stage for building the Maven project
        stage('Build') {
            steps {
                echo 'Building Maven Project'
                sh 'mvn clean compile'  // Clean before compiling for better consistency
            }
        }

        // Stage for running unit tests and generating JaCoCo reports
        stage('Mvn Test') {
            steps {
                echo 'Running Unit Tests'
                sh 'mvn clean test jacoco:report'  // Run tests and generate JaCoCo code coverage report
            }
            post {
                always {
                    // Publish JUnit test results and JaCoCo coverage data
                    junit '**/target/surefire-reports/TEST-*.xml'  // Collect JUnit test results
                    jacoco execPattern: '**/target/jacoco.exec'  // Publish JaCoCo coverage data
                }
            }
        }

        // Stage for running SonarQube static code analysis
        stage('Mvn SonarQube') {
            steps {
                echo 'Running SonarQube Analysis'
                // Trigger SonarQube analysis with the token passed as an environment variable
                sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
            }
        }
    }

    post {
        always {
            // Additional cleanup or steps that should run after all stages (if needed)
            // For example, notifying failure or success of the build (not implemented here)
        }
    }
}
