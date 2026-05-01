package caniuse

import kotlinx.html.*

fun generateIndex(projects: Map<String, Project>, features: Map<String, Feature>): String {
  return generatePage(title = "Can I Use ... in GraphQL?", features = features, projects = projects) {
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
      h2 { +"Projects" }
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
      h2 { +"Features" }

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
