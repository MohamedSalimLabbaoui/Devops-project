pipeline {
    agent any

    tools {
        maven 'maven3'    // Nouveau nom
        jdk 'JDK17'       // Nouveau nom
    }

    stages {
        stage('Checkout') {
            steps {
                echo "📥 Cloning repository..."
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "🔨 Building project..."
                sh 'mvn clean install -DskipTests'
            }
        }
    }

    post {
        always {
            mail to: 'labbaouisalim749@gmail.com',
                 subject: "Pipeline finished: ${currentBuild.currentResult}",
                 body: "Job finished with result: ${currentBuild.currentResult}"
        }
    }
}