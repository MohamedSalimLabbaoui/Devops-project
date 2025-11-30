pipeline {
    agent any

    tools {
        maven 'maven3'
        jdk 'JDK17'
    }

    environment {
        SONARQUBE_URL = 'http://localhost:9000'
        PROJECT_KEY = 'student-management'
        PROJECT_NAME = 'Student Management System'
        EMAIL_TO = 'labbaouisalim749@gmail.com'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "📥 Repository cloned successfully"
            }
        }

        stage('Build & Unit Tests') {
            steps {
                echo "🔨 Building and running unit tests only..."
                sh 'mvn clean test -Dtest="**/*UnitTest.java"'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    echo "📊 Unit test reports generated"
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "🔍 Analyzing code quality with SonarQube..."
                withSonarQubeEnv('sonarqube') {
                    sh """
                    mvn sonar:sonar \
                      -Dsonar.projectKey=${PROJECT_KEY} \
                      -Dsonar.projectName='${PROJECT_NAME}' \
                      -Dsonar.host.url=${SONARQUBE_URL} \
                      -Dsonar.java.binaries=target/classes \
                      -Dsonar.sources=src/main/java \
                      -Dsonar.tests=src/test/java \
                      -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                      -Dsonar.test.inclusions='**/*UnitTest.java'
                    """
                }
            }
        }

        stage('Package') {
            steps {
                echo "📦 Packaging application..."
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            echo "🎓 Pipeline finished: ${currentBuild.currentResult}"
            echo "📈 Build URL: ${env.BUILD_URL}"
        }
        success {
            mail to: "${EMAIL_TO}",
                 subject: "✅ SUCCESS - Student Management Build #${env.BUILD_NUMBER}",
                 body: """
                 Build successful!
                 Unit tests passed and SonarQube analysis completed.

                 Build URL: ${env.BUILD_URL}
                 SonarQube: ${SONARQUBE_URL}/dashboard?id=${PROJECT_KEY}

                 ⚠️ Note: Integration tests were skipped due to configuration issues
                 """
        }
        failure {
            mail to: "${EMAIL_TO}",
                 subject: "❌ FAILED - Student Management Build #${env.BUILD_NUMBER}",
                 body: "Build failed! Check: ${env.BUILD_URL}"
        }
    }
}