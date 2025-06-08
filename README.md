# 🧩 Jenkins Shared Library for EKS CI/CD

This repository contains a **Jenkins Shared Library** used to define and reuse CI/CD pipeline logic across multiple Jenkins jobs. It's specifically designed to manage production-grade deployments to **AWS EKS** using `prodEKSPipeline(configMap)`.
 
## 🚀 How to Use in Jenkinsfile

### 1. Reference the Shared Library

```#!groovy

// declaring a function
def  decidePipeline(Map configMap) {
     type = configMap.get("type")
     switch(type) {
        case "nodejsEKS":
            nodejsEKS(configMap)
            break
        case "nodejsVM":
            nodejsVM(configMap)
            break
        default:
            error "type is not found"
            break
    }
}
