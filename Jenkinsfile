class Constants {
    static final String MASTER_BRANCH = 'main'
    static final String RELEASE_BUILD = 'Release'

    static final String INTERNAL_TRACK = 'internal'
}

def getBuildType() {
    return Constants.RELEASE_BUILD
}

def getTrackType() {
    return Constants.INTERNAL_TRACK
}

pipeline {
    agent any
    environment {
        appName = 'howtank-demo'

        KEY_PASSWORD = credentials('keyPass')
        KEY_ALIAS = credentials('alias')
        KEYSTORE = credentials('keystore')
        STORE_PASSWORD = credentials('storePass')
    }
    stages {
        stage('Build Bundle') {
            when { expression { return isDeployCandidate() } }
            steps {
                echo 'Building'
                script {
                    VARIANT = getBuildType()
                    sh "./gradlew -PstorePass=${STORE_PASSWORD} -Pkeystore=${KEYSTORE} -Palias=${KEY_ALIAS} -PkeyPass=${KEY_PASSWORD} bundle${VARIANT}"
                }
            }
        }
        stage('Deploy App to Store') {
            steps {
                echo 'Deploying'
                script {
                    VARIANT = getBuildType()
                    TRACK = getTrackType()
                    androidApkUpload googleCredentialsId: 'play-store-credentials',
                            filesPattern: "**/outputs/bundle/${VARIANT.toLowerCase()}/*.aab",
                            trackName: TRACK
                }
            }
        }
    }
}