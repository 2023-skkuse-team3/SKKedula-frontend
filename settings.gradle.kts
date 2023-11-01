pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url ="https://jitpack.io")
        // add for naver map
        maven("https://naver.jfrog.io/artifactory/maven/")
        //end
    }
}

rootProject.name = "SKKedula"
include(":app")
 