podTemplate(yaml: '''
    apiVersion: v1
    kind: Pod
    spec:
      containers:
      - name: gradle
        image: gradle:6.3-jdk14
        command:
        - sleep
        args:
        - 99d
        volumeMounts:
        - name: shared-storage
          mountPath: /mnt
      - name: kaniko
        image: gcr.io/kaniko-project/executor:debug
        command:
        - sleep
        args:
        - 9999999
        volumeMounts:
        - name: shared-storage
          mountPath: /mnt
        - name: kaniko-secret
          mountPath: /kaniko/.docker
      restartPolicy: Never
      volumes:
      - name: shared-storage
        persistentVolumeClaim:
          claimName: jenkins-pv-claim
      - name: kaniko-secret
        secret:
            secretName: dockercred
            items:
            - key: .dockerconfigjson
              path: config.json
''') {
  node(POD_LABEL) {
    //withEnv(['container=docker']) {
    stage('Build a gradle project') {
      sh 'git clone https://github.com/satty9675/devops-week7.git'
      container('gradle') {
        stage('Build a gradle project') {
          echo "container Value ${env.container}"
          sh '''
                    pwd
	            cd devops-week7
                    '''
        }
      }
    }
    stage('Run Test'){
	 echo 'Running test Stage'
	 if (env.BRANCH_NAME != "playground") {
	 echo "RUNNING TEST for ${env.BRANCH_NAME} branch"
	}else{
	 echo "SKIPPING TEST for ${env.BRANCH_NAME} branch"
	}	
    }
    stage('Code Coverage'){
         echo 'Running Code Coverage Stage'
         if (env.BRANCH_NAME != "feature") {
         echo "RUNNING Code Coverage for ${env.BRANCH_NAME} branch"
        }else{
         echo "SKIPPING Code Coverage for ${env.BRANCH_NAME} branch"
        }
    }
    stage('Build Java Image') {
     echo 'Running Build Java Image Stage'
     if(env.BRANCH_NAME != "playground"){
      echo "RUNNING Build Java Image for ${env.BRANCH_NAME} branch"
      container('kaniko') {
        stage('Build a Go project') {
          sh '''
            echo 'FROM openjdk:8-jre' > Dockerfile
            echo 'COPY ./calculator-0.0.1-SNAPSHOT.jar app.jar' >> Dockerfile
            echo 'ENTRYPOINT ["java", "-jar", "app.jar"]' >> Dockerfile
            ls /mnt/*jar
            mv /mnt/calculator-0.0.1-SNAPSHOT.jar .
	try{
           if (env.BRANCH_NAME == "feature") {
	     echo "RUNNING packing kaniko container for ${env.BRANCH_NAME} branch"
	     /kaniko/executor --force --context `pwd` --destination techfruity/calculator-feature:0.1
	   }
	   if (env.BRANCH_NAME == "main") {
		echo "RUNNING packing kaniko container for ${env.BRANCH_NAME} branch"
                /kaniko/executor --force --context `pwd` --destination techfruity/calculator:1.0
           }else{
		echo "SKIPPING packing kaniko container for ${env.BRANCH_NAME} branch"
	   }
	   '''
        }
	}catch(Exception E){
		
	  echo "Failed Build Java Image stage"+E
	}
      }
	}else{
	 echo "SKIPPING Build Java Image for ${env.BRANCH_NAME} branch"
      }

    }
    //}
  }
}
