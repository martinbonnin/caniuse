package caniuse

import kotlinx.html.*


fun generateFeature(id: String, feature: Feature, features: Map<String, Feature>, projects: Map<String, Project>): String {
  return generatePage(title = "Can I Use ${feature.name}?", features = features, projects = projects, pathPrefix = "../") {
    h1 {
      +feature.name
      if (feature.experimental) {
        span {
          classes = setOf("badge-experimental")
          attributes["data-tooltip"] = "This feature has not been merged in a specification draft yet"
          attributes["tabindex"] = "0"
          attributes["role"] = "button"
          attributes["aria-label"] = "Experimental: This feature has not been merged in a specification draft yet"
          +"EXPERIMENTAL"
        }
      }
    }
    p { markdown(feature.description) }
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
