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
        def releaseExists = ""
    }
    parameters{
        // Which component you want to deploy 
        // Which environment
    }
    stages{
        stage('Deploy'){
            steps{
                script{
                    // Deploy to specific environment QA, UAT, PERF etc..
                }
            }
        }
        stage('Integration tests'){
            steps{
                // Run Integration tests.
                    }
                }
                script{

                }
            }
        }
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