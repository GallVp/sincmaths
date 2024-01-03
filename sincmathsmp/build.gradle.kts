import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.dokka)
    `maven-publish`
    signing
}

group = "io.github.gallvp"
version = "0.3"

object Meta {
    const val PROJECT_NAME = "sincmaths"
    const val POD_NAME = "SincMaths"
    const val DESC =
        "SincMaths: A Kotlin multi-platform implementation of 2D matrix with signal processing capabilities"
    const val LICENSE = "MIT"
    const val LICENSE_URL = "https://opensource.org/license/MIT/"
    const val GITHUB_REPO = "GallVp/sincmaths"
    const val DEVELOPER = "Usman Rashid"
    const val DEVELOPER_ID = "gallvp"
    const val RELEASE_URL = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
    const val SNAPSHOT_URL = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        publishAllLibraryVariants()
    }

    applyDefaultHierarchyTemplate()

    iosX64 {
        compilations.getByName("main") {
            cinterops {
                val tinyexpr by creating {
                    defFile(project.file("src/nativeInterop/cinterop/tinyexpr.def"))
                    includeDirs.allHeaders(project.file("src/cpp/tinyexpr/include"))
                }

                val wavelib by creating {
                    defFile(project.file("src/nativeInterop/cinterop/wavelib.def"))
                    includeDirs.allHeaders(project.file("src/cpp/wavelib/include"))
                }
            }
        }
    }
    iosArm64 {
        compilations.getByName("main") {
            cinterops {
                val tinyexpr by creating {
                    defFile(project.file("src/nativeInterop/cinterop/tinyexpr.def"))
                    includeDirs.allHeaders(project.file("src/cpp/tinyexpr/include"))
                }

                val wavelib by creating {
                    defFile(project.file("src/nativeInterop/cinterop/wavelib.def"))
                    includeDirs.allHeaders(project.file("src/cpp/wavelib/include"))
                }
            }
        }
    }

    cocoapods {
        summary = Meta.DESC
        homepage = "https://github.com/${Meta.GITHUB_REPO}"
        authors = Meta.DEVELOPER
        name = Meta.POD_NAME
        ios.deploymentTarget = "13.0"
        framework {
            baseName = Meta.POD_NAME
            isStatic = false
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your multiplatform dependencies here
        }
        androidMain {
            dependencies {
                implementation(libs.ejml.simple)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        getByName("androidInstrumentedTest") {
            dependencies {
                implementation(libs.espresso.core)
            }

            dependsOn(commonTest.get())
        }
    }
}

android {
    namespace = "$group.${Meta.PROJECT_NAME}"
    compileSdk = 34
    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    externalNativeBuild {
        cmake {
            path(project.file("src/cpp/CMakeLists.txt"))
        }
    }
}

interface Injected {
    @get:Inject
    val fs: FileSystemOperations
}

tasks.getByName("iosX64Test") {

    val injected = project.objects.newInstance<Injected>()

    doFirst {
        injected.fs.copy {
            from("src/androidInstrumentedTest/resources/io/github/gallvp/sincmaths")
            into("build/bin/iosX64/debugTest")
            include("*.csv")
        }
    }
}

publishing {
    publications.withType<MavenPublication> {

        val publication = this
        val javadocJar =
            tasks.register("${publication.name}JavadocJar", Jar::class) {
                archiveClassifier.set("javadoc")
                archiveBaseName.set("${archiveBaseName.get()}-${publication.name}")

                from(tasks.named("dokkaHtml"))
            }
        artifact(javadocJar)

        pom {
            description.set(Meta.DESC)
            url.set("https://github.com/${Meta.GITHUB_REPO}")
            licenses {
                license {
                    name.set(Meta.LICENSE)
                    url.set(Meta.LICENSE_URL)
                }
            }
            developers {
                developer {
                    id.set(Meta.DEVELOPER_ID)
                    name.set(Meta.DEVELOPER)
                }
            }
            scm {
                url.set(
                    "https://github.com/${Meta.GITHUB_REPO}.git",
                )
                connection.set(
                    "scm:git:git://github.com/${Meta.GITHUB_REPO}.git",
                )
                developerConnection.set(
                    "scm:git:git://github.com/${Meta.GITHUB_REPO}.git",
                )
            }
            issueManagement {
                url.set("https://github.com/${Meta.GITHUB_REPO}/issues")
            }
        }
    }

    repositories {
        maven {
            credentials {
                username = providers.gradleProperty("NEXUS_USERNAME").orNull
                password = providers.gradleProperty("NEXUS_PASSWORD").orNull
            }
            url =
                if ((version as String).endsWith("SNAPSHOT")) uri(Meta.SNAPSHOT_URL) else uri(Meta.RELEASE_URL)
        }
    }
}

publishing.publications.configureEach {
    signing.sign(this)
}
