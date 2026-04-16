package caniuse

import kotlinx.serialization.json.Json
import java.io.File

fun main(args: Array<String>) {
    val outputDir = File("build/site")
    outputDir.mkdirs()

    val json = Json { ignoreUnknownKeys = true }

    val projects: Map<String, Project> = File("data/projects").listFiles()!!
        .filter { it.extension == "json" }
        .associate {
            it.nameWithoutExtension to json.decodeFromString<Project>(it.readText())
        }

    val features: Map<String, Feature> = File("data/features").listFiles()!!
        .filter { it.extension == "json" }
        .associate {
            it.nameWithoutExtension to json.decodeFromString<Feature>(it.readText())
        }


    File("static").copyRecursively(outputDir, overwrite = true)
    outputDir.resolve("index.html").writeText(generateIndex(projects, features))

    val projectDir = outputDir.resolve("project")
    projectDir.mkdirs()
    projects.forEach { (id, project) ->
        projectDir.resolve("$id.html").writeText(generateProject(id, project, features))
    }

    val featureDir = outputDir.resolve("feature")
    featureDir.mkdirs()
    features.forEach { (id, feature) ->
        featureDir.resolve("$id.html").writeText(generateFeature(id, feature, projects))
    }

    println("Loaded ${projects.size} projects and ${features.size} features")
    println("Site generated in ${outputDir.absolutePath}")
}
