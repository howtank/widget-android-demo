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
        appName = 'howtank-widget-demo'

        KEY_PASSWORD = credentials('MOB_KEY_PASSWORD')
        KEY_ALIAS = credentials('MOB_KEY_ALIAS')
        KEYSTORE = credentials('MOB_KEYSTORE')
        STORE_PASSWORD = credentials('MOB_STORE_PASSWORD')
        GRADLE_PROPERTIES = credentials('gradle.properties')
    }
    stages {
        stage('Build Bundle') {
            steps {
                echo 'Building'
                script {
                    VARIANT = getBuildType()
                    sh('rm -rf ./gradle.properties')
                    sh('rm -rf ./keystore.jks')
                    sh('cp -rv $GRADLE_PROPERTIES ./')
                    sh('cp -rv $KEYSTORE ./')
                    sh('./gradlew -PstorePass=$STORE_PASSWORD -Pkeystore=$KEYSTORE -Palias=$KEY_ALIAS -PkeyPass=$KEY_PASSWORD bundle$VARIANT')
                }
            }
        }
        stage('Deploy App to Store') {
            steps {
                echo 'Deploying'
                script {
                    VARIANT = getBuildType()
                    TRACK = getTrackType()
                    androidApkUpload googleCredentialsId: 'android-mobile',
                            filesPattern: "**/outputs/bundle/${VARIANT.toLowerCase()}/*.aab",
                            trackName: TRACK,
                            rolloutPercentage: '100'
                }
            }
        }
    }
}