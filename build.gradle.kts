plugins {
    kotlin("multiplatform") version "1.4.21"
}

group = "me.leihu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val androidFFmpegLibHome = projectDir.resolve("src/thirdParty/ffmpeg")

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget = when {
//        hostOs == "Mac OS X" -> macosX64("native")
//        hostOs == "Linux" -> linuxX64("native")
//        isMingwX64 -> mingwX64("native")
//        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//    }
    val nativeTarget = androidNativeArm64("native")

    nativeTarget.apply {
        compilations["main"].cinterops{
            val ffmpeg by creating{
//                defFile(project.file("src/nativeInterop/cinterop/ffmpeg.def"))
                includeDirs(androidFFmpegLibHome.resolve("include"))
            }
            val avcodec by creating{
                includeDirs(androidFFmpegLibHome.resolve("include"))
            }
        }
        binaries {
            binaries {
                sharedLib("ffmpeg") {
                    linkerOpts("-L${androidFFmpegLibHome.resolve("lib")}", "-lavcodec", "-lavdevice", "-lavfilter", "-lavformat", "-lavutil", "-lswresample", "-lswscale")
                }
            }
//            executable {
//                entryPoint = "main"
//            }
        }
    }
    sourceSets {
        val nativeMain by getting
        val nativeTest by getting
    }
}
