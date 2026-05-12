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
        h2 { anchored("background", "Background") }
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
      h2 { anchored("projects", "Projects") }
      table {
        classes = setOf("support-table", "sortable-table")
        thead {
          tr {
            th {
              attributes["data-sort"] = "string"
              +"Project Name"
            }
            th {
              attributes["data-sort"] = "string"
              +"Type"
            }
            th {
              attributes["data-sort"] = "number"
              +"Not applicable"
            }
            th {
              attributes["data-sort"] = "number"
              attributes["data-sort-default"] = "asc"
              +"Unsupported"
            }
            th {
              attributes["data-sort"] = "number"
              +"Supported"
            }
          }
        }
        tbody {
          sortedProjects(features, projects).forEach {
            tr {
              td {
                a(href = "project/${it.id}.html") { +it.name }
              }
              td { +it.type }
              td { +it.na.toString() }
              td { +it.unsupported.toString() }
              td {
                +if (it.supported % 1.0 == 0.0) it.supported.toInt().toString() else it.supported.toString()
              }
            }
          }
        }
      }
    }
    div {
      h2 { anchored("features", "Features") }
      table {
        classes = setOf("support-table", "sortable-table")
        thead {
          tr {
            th {
              attributes["data-sort"] = "string"
              +"Feature Name"
            }
            th {
              attributes["data-sort"] = "number"
              +"Not Applicable"
            }
            th {
              attributes["data-sort"] = "number"
              +"Unsupported"
            }
            th {
              attributes["data-sort"] = "number"
              attributes["data-sort-default"] = "desc"
              +"Supported"
            }
          }
        }
        tbody {
          sortedFeatures(features, projects).forEach {
            tr {
              td {
                a(href = "feature/${it.id}.html") { +it.name }
                if (it.experimental) {
                  span {
                    classes = setOf("badge-experimental")
                    attributes["title"] = "This feature has not been merged in a specification draft yet"
                    +"E"
                  }
                }
              }
              td { +it.na.toString() }
              td { +it.unsupported.toString() }
              td {
                +if (it.supported % 1.0 == 0.0) it.supported.toInt().toString() else it.supported.toString()
              }
            }
          }
        }
      }
    }
    script {
      unsafe {
        +"""
          (function() {
            document.querySelectorAll('table.sortable-table').forEach(function(table) {
              var headers = Array.from(table.querySelectorAll('thead th[data-sort]'));
              var tbody = table.querySelector('tbody');
              if (!tbody) return;

              function sortBy(header, direction) {
                var idx = headers.indexOf(header);
                var type = header.getAttribute('data-sort');
                var rows = Array.from(tbody.querySelectorAll('tr'));
                rows.sort(function(a, b) {
                  var av = a.children[idx].textContent.trim();
                  var bv = b.children[idx].textContent.trim();
                  if (type === 'number') {
                    av = parseFloat(av); bv = parseFloat(bv);
                    if (isNaN(av)) av = 0;
                    if (isNaN(bv)) bv = 0;
                  } else {
                    av = av.toLowerCase(); bv = bv.toLowerCase();
                  }
                  if (av < bv) return direction === 'asc' ? -1 : 1;
                  if (av > bv) return direction === 'asc' ? 1 : -1;
                  return 0;
                });
                rows.forEach(function(r) { tbody.appendChild(r); });
                headers.forEach(function(h) { h.removeAttribute('aria-sort'); });
                header.setAttribute('aria-sort', direction === 'asc' ? 'ascending' : 'descending');
              }

              headers.forEach(function(h) {
                h.addEventListener('click', function() {
                  var current = h.getAttribute('aria-sort');
                  var direction = current === 'ascending' ? 'desc' : 'asc';
                  sortBy(h, direction);
                });
              });

              var defaultHeader = headers.find(function(h) { return h.hasAttribute('data-sort-default'); });
              if (defaultHeader) {
                defaultHeader.setAttribute('aria-sort', defaultHeader.getAttribute('data-sort-default') === 'desc' ? 'descending' : 'ascending');
              }
            });
          })();
        """.trimIndent()
      }
    }
  }
}

private class DisplayProject(
  val id: String,
  val name: String,
  val type: String,
  val supported: Double,
  val unsupported: Int,
  val na: Int,
)

private class DisplayFeature(
  val id: String,
  val name: String,
  val supported: Double,
  val unsupported: Int,
  val na: Int,
  val experimental: Boolean,
)

private fun sortedProjects(features: Map<String, Feature>, projects: Map<String, Project>): List<DisplayProject> {
  return projects.entries.map { project ->
    project.value.features.keys.forEach {
      if (!features.keys.contains(it)) {
        error("Unkown feature $it in project ${project.key}, please double check ${project.key}.json")
      }
    }
    val statuses = project.value.features.values.map { it.toSupportStatus() }
    DisplayProject(
      id = project.key,
      name = project.value.name,
      type = project.value.type,
      supported = statuses.sumOf { it.score() },
      unsupported = statuses.count { it is NotSupported || it is Unknown },
      na = statuses.count { it is NotApplicable },
    )
  }.sortedWith(
    compareBy<DisplayProject> { it.unsupported }.thenBy { it.name }
  )
}

private fun sortedFeatures(features: Map<String, Feature>, projects: Map<String, Project>): List<DisplayFeature> {
  return features.entries.map { feature ->
    val statuses = projects.values.map { it.features.get(feature.key).toSupportStatus() }
    DisplayFeature(
      id = feature.key,
      name = feature.value.name,
      supported = statuses.sumOf { it.score() },
      unsupported = statuses.count { it is NotSupported || it is Unknown },
      na = statuses.count { it is NotApplicable },
      experimental = feature.value.experimental,
    )
  }.sortedWith(
    compareByDescending<DisplayFeature> { it.supported }.thenBy { it.name }
  )
}
