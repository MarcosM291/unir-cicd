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
    }

    post {
        always {
            echo "The job name is: ${env.JOB_NAME} \
            TEST LINEA 1 \
            TEST LINEA 2 \
            ${env.BUILD_NUMBER}
            "
            echo "The build number is: ${env.BUILD_NUMBER}"
            junit 'results/*_result.xml'
            cleanWs()
        }

         failure {
            script {
                emailext body: 'The pipeline $env.JOB_NAME with build number #$env.BUILD_NUMBER has failed.', 
                recipientProviders: [buildUser()], subject: 'Pipeline Failed: $env.JOB_NAME - Build $env.BUILD_NUMBER'
            }
        }
    }
}
