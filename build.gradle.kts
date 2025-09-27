import org.jetbrains.kotlin.konan.target.Family

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.detekt)
}

group = "com.riversoforion.eyre"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    // From https://stackoverflow.com/a/77964683/115541
    // Initialize all target platforms.
    // Incompatible targets will be automatically skipped with a warning;
    // we suppress such warnings by adding a line to gradle.properties:
    // kotlin.native.ignoreDisabledTargets=true
    val linuxArm64 = linuxArm64()
    val linuxX64 = linuxX64()
    val macosArm64 = macosArm64()
    val macosX64 = macosX64()
    val windows = mingwX64("windows")

    // Full cross-compilation seems to work, at least on Mac
    // TODO Experiment on Linux host
    // If it works, then we can enable all targets unconditionally
    // Update the release task to build for all targets on a single pass
    // Create new OS-specific build tasks that can be used by a local dev environment

    // Configure which native targets to build, based on current platform.
    val hostOs = System.getProperty("os.name")
    val isCi = System.getenv("CI").toBoolean()
    val nativeTargets = when {
        isCi                         -> listOf(linuxArm64, linuxX64, macosArm64, macosX64, windows)
        hostOs == "Linux"            -> listOf(linuxArm64, linuxX64)
        hostOs == "Mac OS X"         -> listOf(macosArm64, macosX64)
        hostOs.startsWith("Windows") -> listOf(windows)
        else                         -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    // For now, stick with the default "common" source set. We can also define platform-specific source sets, though
    // that introduces a lot of complexity. See early revisions of this file for an example. For more details, see:
    // https://kotlinlang.org/docs/multiplatform-advanced-project-structure.html#declaring-custom-source-sets
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinxSerializationJson)
            }
        }
        commonTest {
            dependencies {}
        }
    }

    // Configure the binary executables for all activated native targets
    nativeTargets.forEach {
        it.apply {
            binaries {
                executable {
                    entryPoint = "main"
                }
            }
        }
    }

    // Define packaging tasks for all activated native targets
    val executables = nativeTargets.associateWith { it.binaries.findExecutable("RELEASE") }
    val execPlatforms = executables.values.filterNotNull().associate { exec ->
        exec.target.name to
                "${exec.target.konanTarget.family}-${exec.target.konanTarget.architecture}".lowercase()
    }
    val distTaskNames = mutableListOf<String>()
    val distDir = layout.buildDirectory.dir("dist")

    nativeTargets.forEach {
        val taskName = "${it.name}DistPkg"
        distTaskNames.add(taskName)

        if (it.konanTarget.family == Family.MINGW) {
            tasks.register<Zip>(taskName) {
                group = "distribution"
                val exec = executables[it] ?: throw GradleException("No executable for target ${it.name}")
                val platform = execPlatforms[it.name] ?: throw GradleException("No architecture for target ${it.name}")
                dependsOn(exec.linkTaskName)
                from(exec.outputFile) {
                    rename { "eyre.exe" }
                }
                destinationDirectory.set(distDir)
                archiveFileName.set("eyre-$platform.zip")
                doLast {
                    logger.lifecycle("Packaged ${archiveFile.get().asFile} containing eyre.exe")
                }
            }
        }
        else {
            tasks.register<Tar>(taskName) {
                group = "distribution"
                val exec = executables[it] ?: throw GradleException("No executable for target ${it.name}")
                val platform = execPlatforms[it.name] ?: throw GradleException("No architecture for target ${it.name}")
                dependsOn(exec.linkTaskName)
                from(exec.outputFile) {
                    rename { "eyre" }
                    filePermissions {
                        user { read = true; write = true; execute = true }
                        group { read = true; execute = true }
                        other { read = true; execute = true }
                    }
                }
                destinationDirectory.set(distDir)
                archiveFileName.set("eyre-$platform.tar.gz")
                compression = Compression.GZIP
                doLast {
                    logger.lifecycle("Packaged ${archiveFile.get().asFile} containing eyre")
                }
            }
        }
    }

    // Register the release task
    tasks.register("release") {
        group = "distribution"
        val buildTaskNames = nativeTargets.mapNotNull {
            it.binaries
                .findExecutable("RELEASE")
                ?.linkTaskName
        }
        dependsOn(buildTaskNames, distTaskNames)
        doLast {
            logger.lifecycle("Release build completed.")
        }
    }
}

detekt {
    toolVersion = "1.23.8"
    buildUponDefaultConfig = true
    config.setFrom(file("$rootDir/detekt.yaml"))
    source.setFrom(files("src"))
}
