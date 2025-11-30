pipeline {
    agent any

    tools {
        maven 'maven3'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                echo "🔨 Building and running all tests..."
                sh 'mvn clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "🔍 Analyzing code quality with SonarQube..."
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=student-management'
                }
            }
        }
    }

    post {
        always {
            echo "🎓 Pipeline finished: ${currentBuild.currentResult}"
        }
        success {
            mail to: 'labbaouisalim749@gmail.com',
                 subject: "✅ SUCCESS - Student Management Build #${env.BUILD_NUMBER}",
                 body: """
                 🎓 Student Management System - Build Successful!

                 ✅ All tests passed
                 ✅ SonarQube analysis completed

                 Build URL: ${env.BUILD_URL}
                 SonarQube: http://localhost:9000
                 """
        }
        failure {
            mail to: 'labbaouisalim749@gmail.com',
                 subject: "❌ FAILED - Student Management Build #${env.BUILD_NUMBER}",
                 body: "Build failed! Check: ${env.BUILD_URL}"
        }
    }
}