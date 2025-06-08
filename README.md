# Jenkins Shared Library for EKS&VM CI/CD

This repository contains a **Jenkins Shared Library** used to define and reuse CI/CD pipeline logic across multiple Jenkins jobs. It's specifically designed to manage production-grade deployments to **AWS EKS and VM** using `prodEKSPipeline(configMap)`.
 
## How to Use in Jenkinsfile

### 1. Reference the Shared Library

```#!groovy

@Library('jenkins-shared-library') _

// create variable of map type and set the values

def configMap = [
    type: "nodejsEKS"
    component: "backend",
    project: "expense"
]

if ( ! env.BRANCH_NAME.equalsIgnoreCase('main')){
    pipelineDecission.decidePipeline(configMap)
}
else{
    echo "Proceed with CR or Non-Prod pipeline"
}
