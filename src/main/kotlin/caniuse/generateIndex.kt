package caniuse

import kotlinx.html.*

fun generateIndex(projects: Map<String, Project>, features: Map<String, Feature>): String {
  return generatePage(features = features, projects = projects) {
    div {
      h1 { +"Can I use ... in GraphQL ?" }
      p {
        +"A directory of (mostly) experimental GraphQL features and the state of their support accross different projects."
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
        b {
          +"Note:"
        }
        +" This website is still under construction and data is still **very** incomplete."
      }
    }
    div {
      id = "columns"
      div {
        h2 { +"Features" }
        p {
          +"All features sorted by the number of projects that support them.*"
        }

        val maxScore = projects.size
        sortedFeatures(features, projects).forEach {
          val supportedPercent = if (maxScore > 0) (it.supported * 100) / maxScore else 0
          val naPercent = if (maxScore > 0) (it.na * 100) / maxScore else 0
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
              if (it.experimental) {
                span {
                  classes = setOf("tag-experimental-small")
                  attributes["title"] = "This feature has not been merged in a specification draft yet"
                  +"E"
                }
              }
            }
            span {
              classes = setOf("feature-bar-score")
              +"${it.total} / $maxScore"
            }
          }
        }
      }
      div {
        h2 { +"Projects" }
        p {
          +"All projects sorted by the number of features they support.*"
        }
        val maxFeatures = features.size
        sortedProjects(features, projects).forEach {
          val supportedPercent = if (maxFeatures > 0) (it.supported * 100) / maxFeatures else 0
          val naPercent = if (maxFeatures > 0) (it.na * 100) / maxFeatures else 0
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
    }
    p {
      classes = setOf("footnote")
      +"* Because not all features make sense for all projects, and so as not to disadvantage projects with a narrower scope, the total score includes features that are not applicable. For an example, graphql-js is not concerned about "
      code {
        +"application/graphql-response+json"
      }
      +" but it still counts towards its global score."
      br
      +"In each bar, the features that are not applicable, are displayed in a dimmed color."
    }
  }
}

private class DisplayProject(
  val id: String,
  val name: String,
  val supported: Int,
  val na: Int
) {
  val total = supported + na
}

private class DisplayFeature(
  val id: String,
  val name: String,
  val supported: Int,
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
      project.value.features.count {
        if (!features.keys.contains(it.key)) {
          error("Unkown feature ${it.key} it project ${project.key}, please double check ${project.key}.json")
        }
        it.value?.toSupportStatus() is Supported
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
      projects.values.count {
        it.features.get(feature.key).toSupportStatus() is Supported
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

private fun SupportInfo.isSupportedForDisplay(): Boolean {
  return when (toSupportStatus()) {
    is Supported -> true
    NotApplicable -> true
    NotSupported -> false
    Unknown -> false
  }
}