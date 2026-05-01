package caniuse

import kotlinx.html.*

fun generateIndex(projects: Map<String, Project>, features: Map<String, Feature>): String {
  return generatePage(title = "Can I Use ... in GraphQL?", features = features, projects = projects, pathPrefix = "./") {
    div {
      h1 { +"Can I use ... in GraphQL ?" }
      p {
        +"A directory of newer GraphQL features, both drafted and experimental, and the state of their support across different projects."
      }
      p {
        +"The data for this website is open source ("
        a(href = "https://github.com/$repo") {
          +"GitHub repository"
        }
        +") and contributions are warmly welcome. Please see "
        a(href = "https://github.com/$repo/blob/main/CONTRIBUTING.md") {
          +"CONTRIBUTING.md"
        }
        + " for how to add a new project, feature, or to add an existing feature to an existing project."
      }

      p {
        h2 {
          id = "background"
          classes = setOf("anchor-heading")
          a(href = "#background") {
            classes = setOf("heading-anchor")
            attributes["aria-label"] = "Link to Background"
            unsafe {
              +"""<svg viewBox="0 0 16 16" width="16" height="16" fill="currentColor" aria-hidden="true"><path d="M7.775 3.275a.75.75 0 0 0 1.06 1.06l1.25-1.25a2 2 0 1 1 2.83 2.83l-2.5 2.5a2 2 0 0 1-2.83 0 .75.75 0 0 0-1.06 1.06 3.5 3.5 0 0 0 4.95 0l2.5-2.5a3.5 3.5 0 0 0-4.95-4.95l-1.25 1.25Zm-4.69 9.64a2 2 0 0 1 0-2.83l2.5-2.5a2 2 0 0 1 2.83 0 .75.75 0 0 0 1.06-1.06 3.5 3.5 0 0 0-4.95 0l-2.5 2.5a3.5 3.5 0 0 0 4.95 4.95l1.25-1.25a.751.751 0 0 0-.018-1.042.751.751 0 0 0-1.042-.018l-1.25 1.25a2 2 0 0 1-2.83 0Z"/></svg>"""
            }
          }
          +"Background"
        }
      }
      p {
        +"GraphQL is a wonderful piece of technology. The GraphQL spec has been a very solid foundation for the past 10 years. It's not (yet!) perfect though. Friction points exist and the community has been "
        a(href = "https://github.com/graphql/graphql-wg/", target = "blank") {
          +"hard at work"
        }
        +" improving the daily GraphQL experience."
      }
      p {
        +"Most of the friction points have identified solutions in the form of RFCs. Some of them have made it to the spec (`@oneOf`, schema coordinates, ...). Others remain in experimental state. "
        +"As time passes, it's becoming harder and harder to tell what feature is implemented where. This website bridges that "
        a(href = "https://github.com/graphql/gaps/") { +"GAP" }
        +"."
      }
    }
    div {
      h2 {
        id = "projects"
        classes = setOf("anchor-heading")
        a(href = "#projects") {
          classes = setOf("heading-anchor")
          attributes["aria-label"] = "Link to Projects"
          unsafe {
            +"""<svg viewBox="0 0 16 16" width="16" height="16" fill="currentColor" aria-hidden="true"><path d="M7.775 3.275a.75.75 0 0 0 1.06 1.06l1.25-1.25a2 2 0 1 1 2.83 2.83l-2.5 2.5a2 2 0 0 1-2.83 0 .75.75 0 0 0-1.06 1.06 3.5 3.5 0 0 0 4.95 0l2.5-2.5a3.5 3.5 0 0 0-4.95-4.95l-1.25 1.25Zm-4.69 9.64a2 2 0 0 1 0-2.83l2.5-2.5a2 2 0 0 1 2.83 0 .75.75 0 0 0 1.06-1.06 3.5 3.5 0 0 0-4.95 0l-2.5 2.5a3.5 3.5 0 0 0 4.95 4.95l1.25-1.25a.751.751 0 0 0-.018-1.042.751.751 0 0 0-1.042-.018l-1.25 1.25a2 2 0 0 1-2.83 0Z"/></svg>"""
          }
        }
        +"Projects"
      }
      val maxFeatures = features.size
      sortedProjects(features, projects).forEach {
        val supportedPercent = if (maxFeatures > 0) (it.supported * 100.0) / maxFeatures else 0.0
        val naPercent = if (maxFeatures > 0) (it.na * 100.0) / maxFeatures else 0.0
        a(href = "project/${it.id}.html") {
          classes = setOf("project-bar")
          attributes["title"] = "${it.supported} supported, ${it.na} not applicable"
          div {
            classes = setOf("project-bar-fill")
            style = "width: ${supportedPercent}%"
          }
          div {
            classes = setOf("project-bar-fill-na")
            style = "left: ${supportedPercent}%; width: ${naPercent}%"
          }
          span {
            classes = setOf("project-bar-label")
            +it.name
          }
          span {
            classes = setOf("project-bar-score")
            +"${it.total} / $maxFeatures"
          }
        }
      }
    }
    div {
      h2 {
        id = "features"
        classes = setOf("anchor-heading")
        a(href = "#features") {
          classes = setOf("heading-anchor")
          attributes["aria-label"] = "Link to Features"
          unsafe {
            +"""<svg viewBox="0 0 16 16" width="16" height="16" fill="currentColor" aria-hidden="true"><path d="M7.775 3.275a.75.75 0 0 0 1.06 1.06l1.25-1.25a2 2 0 1 1 2.83 2.83l-2.5 2.5a2 2 0 0 1-2.83 0 .75.75 0 0 0-1.06 1.06 3.5 3.5 0 0 0 4.95 0l2.5-2.5a3.5 3.5 0 0 0-4.95-4.95l-1.25 1.25Zm-4.69 9.64a2 2 0 0 1 0-2.83l2.5-2.5a2 2 0 0 1 2.83 0 .75.75 0 0 0 1.06-1.06 3.5 3.5 0 0 0-4.95 0l-2.5 2.5a3.5 3.5 0 0 0 4.95 4.95l1.25-1.25a.751.751 0 0 0-.018-1.042.751.751 0 0 0-1.042-.018l-1.25 1.25a2 2 0 0 1-2.83 0Z"/></svg>"""
          }
        }
        +"Features"
      }

      val maxScore = projects.size
      sortedFeatures(features, projects).forEach {
        val supportedPercent = if (maxScore > 0) (it.supported * 100.0) / maxScore else 0.0
        val naPercent = if (maxScore > 0) (it.na * 100.0) / maxScore else 0.0
        a(href = "feature/${it.id}.html") {
          classes = setOf("feature-bar")
          attributes["title"] = "${it.supported} supported, ${it.na} not applicable"
          div {
            classes = setOf("feature-bar-fill")
            style = "width: ${supportedPercent}%"
          }
          div {
            classes = setOf("feature-bar-fill-na")
            style = "left: ${supportedPercent}%; width: ${naPercent}%"
          }
          span {
            classes = setOf("feature-bar-label")
            +it.name
          }
          if (it.experimental) {
            span {
              classes = setOf("feature-bar-experimental")
              attributes["title"] = "This feature has not been merged in a specification draft yet"
              +"🧪"
            }
          }
          span {
            classes = setOf("feature-bar-score")
            +"${it.total} / $maxScore"
          }
        }
      }
    }
  }
}

private class DisplayProject(
  val id: String,
  val name: String,
  val supported: Double,
  val na: Int
) {
  val total = supported + na
}

private class DisplayFeature(
  val id: String,
  val name: String,
  val supported: Double,
  val na: Int,
  val experimental: Boolean
) {
  val total = supported + na
}

private fun sortedProjects(features: Map<String, Feature>, projects: Map<String, Project>): List<DisplayProject> {
  return projects.entries.map { project ->
    DisplayProject(
      project.key,
      project.value.name,
      project.value.features.entries.sumOf {
        if (!features.keys.contains(it.key)) {
          error("Unkown feature ${it.key} in project ${project.key}, please double check ${project.key}.json")
        }
        it.value?.toSupportStatus().score()
      },
      project.value.features.count {
        it.value?.toSupportStatus() is NotApplicable
      }
    )
  }.sortedWith(
    compareBy<DisplayProject> {
      it.total
    }.thenBy {
      it.supported
    }.reversed()
      .thenBy {
      it.name
    }
  )
}

private fun sortedFeatures(features: Map<String, Feature>, projects: Map<String, Project>): List<DisplayFeature> {
  return features.entries.map { feature ->
    DisplayFeature(
      feature.key,
      feature.value.name,
      projects.values.sumOf {
        it.features.get(feature.key).toSupportStatus().score()
      },
      projects.values.count {
        it.features.get(feature.key).toSupportStatus() is NotApplicable
      },
      feature.value.experimental
    )
  }.sortedWith(
    compareBy<DisplayFeature> {
      it.total
    }.thenBy {
      it.supported
    }.reversed()
      .thenBy {
        it.name
      }
  )
}
