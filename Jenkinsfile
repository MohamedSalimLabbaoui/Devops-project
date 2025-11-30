pipeline {
    agent any

    tools {
        maven 'maven3'
        jdk 'JDK17'
    }

    environment {
        SONARQUBE_URL = 'http://localhost:9000'
        PROJECT_KEY = 'student-management'
        EMAIL_TO = 'labbaouisalim749@gmail.com'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "📥 Repository cloned successfully"
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
                    echo "📊 Test reports generated"
                }
            }
        }

       stage('SonarQube Analysis') {
           steps {
               echo "🔍 Analyzing code quality with SonarQube..."
               script {
                   withSonarQubeEnv('sonarqube') {
                       sh """
                       mvn sonar:sonar \
                         -Dsonar.projectKey=student-management \
                         -Dsonar.host.url=http://localhost:9000 \
                         -Dsonar.java.binaries=target/classes \
                         -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                       """
                   }
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
            echo "👤 Executed by: ${env.CHANGE_AUTHOR}"
        }
        success {
            script {
                mail to: "${EMAIL_TO}",
                     subject: "✅ SUCCESS - Student Management Build #${env.BUILD_NUMBER}",
                     body: """
                     🎓 Student Management System - Build Successful!

                     ✅ Build: ${env.JOB_NAME} #${env.BUILD_NUMBER}
                     ✅ Status: ALL TESTS PASSED
                     ✅ SonarQube analysis completed
                     ✅ Application packaged successfully

                     📊 Details:
                     - Build URL: ${env.BUILD_URL}
                     - SonarQube: ${SONARQUBE_URL}
                     - Project: ${PROJECT_KEY}
                     - Branch: ${env.GIT_BRANCH}

                     🚀 Ready for deployment!
                     """
                echo "📧 Success email sent to ${EMAIL_TO}"
            }
        }
        failure {
            script {
                mail to: "${EMAIL_TO}",
                     subject: "❌ FAILED - Student Management Build #${env.BUILD_NUMBER}",
                     body: """
                     🎓 Student Management System - Build Failed!

                     ❌ Build: ${env.JOB_NAME} #${env.BUILD_NUMBER}
                     ❌ Status: TESTS FAILED OR BUILD ERROR

                     🔍 Investigation needed:
                     - Build URL: ${env.BUILD_URL}
                     - Check test reports in Jenkins
                     - Verify code changes

                     ⚠️ Please fix the issues and retry the build.
                     """
                echo "📧 Failure email sent to ${EMAIL_TO}"
            }
        }
        unstable {
            script {
                mail to: "${EMAIL_TO}",
                     subject: "⚠️ UNSTABLE - Student Management Build #${env.BUILD_NUMBER}",
                     body: """
                     🎓 Student Management System - Build Unstable!

                     ⚠️ Build: ${env.JOB_NAME} #${env.BUILD_NUMBER}
                     ⚠️ Status: SOME TESTS FAILED

                     📊 Details:
                     - Build URL: ${env.BUILD_URL}
                     - Check test reports for flaky tests
                     - Some tests may need investigation

                     🔧 Consider reviewing test stability.
                     """
                echo "📧 Unstable email sent to ${EMAIL_TO}"
            }
        }
    }
}