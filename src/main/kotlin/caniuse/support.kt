package caniuse

internal class SupportEntry(
  val link: String,
  val name: String,
  val label: String,
  val note: Markdown?,
  val status: SupportStatus
)

sealed interface SupportStatus
class Supported(val version: String) : SupportStatus
object NotApplicable : SupportStatus
object Unknown : SupportStatus
object NotSupported : SupportStatus

internal fun featureEntries(id: String, project: Project, features: Map<String, Feature>): List<SupportEntry> {
  return features.map { feature ->
    project.features.get(feature.key).toSupportEntry(id, feature.value.name, "../feature/${feature.key}.html")
  }.sortedBy { it.name }
}

internal fun projectEntries(id: String, projects: Map<String, Project>): List<SupportEntry> {
  return projects.map { project ->
    project.value.features.get(id).toSupportEntry(id, project.value.name, "../project/${project.key}.html")
  }.sortedBy { it.name }
}

internal fun SupportInfo?.toSupportStatus(): SupportStatus {
  return if (this?.since == null || since == "?") {
    Unknown
  } else if (since == "n/a") {
    NotApplicable
  } else if (since == "-") {
    NotSupported
  } else {
    Supported(since)
  }
}

private fun SupportInfo?.toSupportEntry(id: String, name: String, link: String): SupportEntry {
  val status = toSupportStatus()

  val note = if (this?.since == null) {
    "Know the status of this feature? [Let use know!](https://github.com/$repo/edit/main/data/projects/$id.json)"
  } else {
    note
  }

  val label = if (this?.since == null || since == "?") {
    "?"
  } else if (since == "n/a") {
    "N/A"
  } else if (since == "-") {
    "-"
  } else {
    since
  }
  return SupportEntry(
    link,
    name,
    label,
    note?.let { Markdown(it) },
    status
  )
}

