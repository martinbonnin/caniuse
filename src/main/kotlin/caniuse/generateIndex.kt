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
          +"All features sorted by the number of projects that support them."
        }

        val maxScore = projects.size
        sortedFeatures(features, projects).forEach {
          val percent = if (maxScore > 0) (it.score * 100) / maxScore else 0
          a(href = "feature/${it.id}.html") {
            classes = setOf("feature-bar")
            div {
              classes = setOf("feature-bar-fill")
              style = "width: ${percent}%"
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
              +"${it.score} / $maxScore"
            }
          }
        }
      }
      div {
        h2 { +"Projects" }
        p {
          +"All projects sorted by the number of features they support."
        }
        val maxFeatures = features.size
        sortedProjects(features, projects).forEach {
          val percent = if (maxFeatures > 0) (it.score * 100) / maxFeatures else 0
          a(href = "project/${it.id}.html") {
            classes = setOf("project-bar")
            div {
              classes = setOf("project-bar-fill")
              style = "width: ${percent}%"
            }
            span {
              classes = setOf("project-bar-label")
              +it.name
            }
            span {
              classes = setOf("project-bar-score")
              +"${it.score} / $maxFeatures"
            }
          }
        }
      }
    }
    p {
      +"Features that make no sense for a given project are considered compatible. For an example, for this chart, graphql-js is considered as supporting "
      code {
        +"application/graphql-response+json"
      }
      +" although it technically doesn't make sense because graphql-js is transport agnostic."
    }
  }
}

private class DisplayProject(
  val id: String,
  val name: String,
  val score: Int
)

private class DisplayFeature(
  val id: String,
  val name: String,
  val score: Int,
  val experimental: Boolean
)

private fun sortedProjects(features: Map<String, Feature>, projects: Map<String, Project>): List<DisplayProject> {
  return projects.entries.map { project ->
    DisplayProject(
      project.key,
      project.value.name,
      project.value.features.count {
        if (!features.keys.contains(it.key)) {
          error("Unkown feature ${it.key} it project ${project.key}, please double check ${project.key}.json")
        }
        it.value?.isSupportedForDisplay() == true
      }
    )
  }.sortedByDescending { it.score }
}

private fun sortedFeatures(features: Map<String, Feature>, projects: Map<String, Project>): List<DisplayFeature> {
  return features.entries.map { feature ->
    DisplayFeature(
      feature.key,
      feature.value.name,
      projects.values.count {
        it.features.get(feature.key)?.isSupportedForDisplay() == true
      },
      feature.value.experimental
    )
  }.sortedByDescending { it.score }
}

private fun SupportInfo.isSupportedForDisplay(): Boolean {
  return when (toSupportStatus()) {
    is Supported -> true
    NotApplicable -> true
    NotSupported -> false
    Unknown -> false
  }
}