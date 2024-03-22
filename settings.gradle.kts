rootProject.name = "spotify-client-desktop-gui"

include(":main")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            val versions = object {
                val java = version("java", "20")
                val kotlin = version("kotlin", "1.9.22")
                val ktor = version("ktor", "2.3.2")
                val okhttp = version("okhttp", "4.10.0")
                val coroutines = version("coroutines", "1.8.0")
                val logback = version("logback", "1.4.14")
                val compose = version("compose", "1.6.1")
            }

            plugin("kotlin.jvm", "org.jetbrains.kotlin.jvm").versionRef(versions.kotlin)
            plugin("compose", "org.jetbrains.compose").versionRef(versions.compose)
            plugin("kotlin.serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef(versions.kotlin)

            library("logback", "ch.qos.logback", "logback-classic").versionRef(versions.logback)
            library("json", "org.json:json:20240303")
            library("stream.log", "io.getstream", "stream-log-android").version("1.1.3")

            library("ktor.serialization", "io.ktor", "ktor-serialization-kotlinx-json").versionRef(versions.ktor)

            library("ktor.server.core", "io.ktor", "ktor-server-core").versionRef(versions.ktor)
            library("ktor.server.netty", "io.ktor", "ktor-server-netty").versionRef(versions.ktor)
            library("ktor.server.logging", "io.ktor", "ktor-server-call-logging").versionRef(versions.ktor)

            library("ktor.client.core", "io.ktor", "ktor-client-core").versionRef(versions.ktor)
            library("ktor.client.coreJvm", "io.ktor", "ktor-client-core-jvm").versionRef(versions.ktor)
            library("ktor.client.cio", "io.ktor", "ktor-client-cio").versionRef(versions.ktor)
            library("ktor.client.logging", "io.ktor", "ktor-client-logging").versionRef(versions.ktor)
            library("ktor.client.loggingJvm", "io.ktor", "ktor-client-logging-jvm").versionRef(versions.ktor)
            library("ktor.client.contentNegotiation", "io.ktor", "ktor-client-content-negotiation").versionRef(versions.ktor)
            library("ktor.client.websockets", "io.ktor", "ktor-client-websockets").versionRef(versions.ktor)

            library("coroutines.core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef(versions.coroutines)
        }
    }
}