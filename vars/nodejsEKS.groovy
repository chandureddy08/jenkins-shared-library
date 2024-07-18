def call(Map configMap){
    pipeline {
    agent {
        label 'AGENT-1'
    }
    options{
        timeout(time: 10, unit: 'MINUTES')
        disableConcurrentBuilds()
        ansiColor('xterm')
    }
    environment{
        def appVersion = '' //variable declaration
        nexusUrl = pipelineGlobals.nexusURL()
        region = pipelineGlobals.region()
        account_id = pipelineGlobals.account_id()
        component = configMap.get("component")
        project  = configMap.get("project")
    }
    stages{
        stage('read the version'){
            steps{
                script{
                    def packageJson = readJSON file: 'package.json'
                    appVersion = packageJson.version
                    echo "application version: $appVersion"
                }
            }
        }
        stage('Install Dependencies') {
            steps{
            sh """
            npm install
            ls -ltr
            echo "application version: $appVersion"
            """
            }
        }
        stage('Build'){
            steps{
            sh """
            zip -q -r ${component}-${appVersion}.zip * -x Jenkinsfile -x backend-${appVersion}.zip
            ls -ltr
                """
            }
        }
        stage('Docker build'){
            steps{
                sh """
                    aws ecr get-login-password --region ${region} | docker login --username
                    AWS --password-stdin ${account_id}.dkr.ecr.${region}.amazonaws.com

                    docker build -t ${account_id}.dkr.ecr.${region}.amazonaws.com/${pRroject}-${component}:${appVersion} .

                    docker push ${account_id}.dkr.ecr.${region}.amazonaws.com/${project}-${component}:${appVersion}
                """
            }
        }
        stage('Deploy'){
            steps{
                sh """
                    aws eks update-kubeconfig --region us-east-1 --name expense-dev
                    cd helm
                    sed -i 's/IMAGE_VERSION/${appVersion}' values.yaml
                    helm intall backend -n ${project} .

                """
            }
        }
        // stage('sonar scan'){
        //     environment{
        //         scannerHome = tool 'sonar-6.0' //refering scanner agent in jenkins
        //     }
        //     steps{
        //         script{
        //             withSonarQubeEnv('sonar-6.0') { //refering sonar server
        //                 sh "${scannerHome}/bin/sonar-scanner"
        //             }
        //         } 
        //     }
        // }
        // stage('Nexus artifact upload'){
        //     steps{
        //     script{
        //             nexusArtifactUploader(
        //                 nexusVersion: 'nexus3',
        //                 protocol: 'http',
        //                 nexusUrl: "${nexusUrl}",
        //                 groupId: 'com.expense',
        //                 version: "${appVersion}",
        //                 repository: "backend",
        //                 credentialsId: 'nexus-auth',
        //                 artifacts: [
        //                     [artifactId: "backend" ,
        //                     classifier: '',
        //                     file: "backend-" + "${appVersion}" + '.zip',
        //                     type: 'zip']
        //                 ]
        //             )
        //     }
        //     }
        // }
    //     stage('Deploy'){
    //         when{
    //             expression{
    //                 params.deploy
    //             }
    //         }
    //         steps{
    //             script{
    //                 def params = [
    //                     string(name: 'appVersion', value: "${appVersion}")
    //                 ]
    //                 build job: 'backend-deploy', parameters: params, wait: false
    //             }
    //         }
    //     }
    // }
        post { 
        always { 
            echo 'I will always say Hello again!'
            deleteDir()
        }
        success { 
            echo 'I will run when pipeline is success'
        }
        failure { 
            echo 'I will run when pipeline is failure'
        }
    }
}
}