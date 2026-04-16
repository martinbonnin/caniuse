package caniuse

import kotlinx.html.*

@JvmInline
internal value class Markdown(val value: String)

internal fun generateProject(id: String, project: Project, features: Map<String, Feature>): String {
  return generatePage(pathPrefix = "../") {
    h1 { +project.name }
    p { +project.description }
    p {
      b { +"Type: " }
      +project.type
    }
    p {
      b { +"Website: " }
      a(href = project.url) { +project.url }
    }
    h2 { +"Features" }
    appendTable("Feature Name", featureEntries(id, project, features))
  }
}