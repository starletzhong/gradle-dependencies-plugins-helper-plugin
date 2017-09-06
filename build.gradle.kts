import org.gradle.kotlin.dsl.*
import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.1.4-3"
    id("org.jetbrains.intellij") version "0.2.17"
}
group = "cn.bestwu"
version = "0.1.0"
intellij {
    updateSinceUntilBuild = false
//    downloadSources = false
//    version ="IC-2017.2.3"
    version = "2016.1"
    setPlugins("Groovy", "gradle", "Kotlin", "maven")
}

tasks.withType(PublishTask::class.java) {
    username(project.property("intellij.publish.username"))
    password(project.property("intellij.publish.password"))
    channels("stable")
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))
    compile("org.jsoup:jsoup:1.10.3")

    testCompile(kotlin("test-junit"))
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    apiVersion = "1.1"
}

