package caniuse

import kotlinx.html.*


fun generateFeature(id: String, feature: Feature, projects: Map<String, Project>): String {
  return generatePage(pathPrefix = "../") {
    h1 { +feature.name }
    p { +feature.description }
    if (feature.url != null) {
      p {
        b { +"More info: " }
        a(href = feature.url) { +feature.url }
      }
    }
    h2 { +"Projects" }
    appendTable("Project Name", projectEntries(id, projects))
  }
}
