plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "caniuse"
version = "0.1.0"

dependencies {
    implementation(libs.flexmark.all)
    implementation(libs.kotlinx.html)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(kotlin("test"))
}

tasks.register("buildWebsite", JavaExec::class.java) {
    classpath(kotlin.target.compilations.getByName("main").output.classesDirs)
    classpath(configurations.named("runtimeClasspath"))
    mainClass.set("caniuse.MainKt")

    inputs.files(fileTree("static")).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("static")
    inputs.files(fileTree("data")).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("data")
}
