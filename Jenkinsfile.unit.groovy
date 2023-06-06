pipeline {
    agent any
    
    stages {
        stage('Source') {
            steps {
                git 'https://github.com/srayuso/unir-cicd.git'
            }
        }
        stage('Build') {
            steps {
                echo 'Building stage!'
                sh 'make build'
            }
        }
        stage('Unit tests') {
            steps {
                sh 'make test-unit'
            }
        }
        stage('API tests') {
            steps {
                sh 'make test-api'
            }
        }
        stage('E2E tests') {
            steps {
                sh 'make test-e2e'
            }
        }
        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'results/*.xml'
            }
    }
    
    post {
        always {
            junit 'results/*_result.xml'
            cleanWs()
        }
    }
}






