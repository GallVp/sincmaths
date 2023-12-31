plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.dokka)
    `maven-publish`
}

group = "io.github.gallvp"
val artifactID = "sincmaths"
val podName = "SincMaths"
version = "0.3"

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
        summary = "sincmaths kotlin multiplatform library"
        homepage = "https://github.com/GallVp/sincmaths"
        authors = "Usman Rashid"
        name = podName
        ios.deploymentTarget = "13.0"
        framework {
            baseName = podName
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
    namespace = "$group.$artifactID"
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
        val artifactId = artifactID
        if (name == "kotlinMultiplatform") {
            setArtifactId(artifactId)
        } else {
            setArtifactId("$artifactId-$name")
        }
    }
}
