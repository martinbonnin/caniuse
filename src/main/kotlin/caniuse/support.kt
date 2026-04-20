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
class Partial(val version: String) : SupportStatus
object NotApplicable : SupportStatus
object Unknown : SupportStatus
object NotSupported : SupportStatus

internal fun featureEntries(id: String, project: Project, features: Map<String, Feature>): List<SupportEntry> {
  return features.map { feature ->
    project.features.get(feature.key).toSupportEntry(id, feature.value.name, "../feature/${feature.key}")
  }.sortedBy { it.name }
}

internal fun projectEntries(id: String, projects: Map<String, Project>): List<SupportEntry> {
  return projects.map { project ->
    project.value.features.get(id).toSupportEntry(id, project.value.name, "../project/${project.key}")
  }.sortedBy { it.name }
}

internal fun SupportStatus?.score(): Double = when(this) {
  is Partial -> 0.5
  is Supported -> 1.0
  else -> 0.0
}

internal fun SupportInfo?.toSupportStatus(): SupportStatus {
  return if (this?.since == null || since == "?") {
    Unknown
  } else if (since == "n/a") {
    NotApplicable
  } else if (since == "-") {
    NotSupported
  } else if (since.endsWith("(partial)")) {
    Partial(since.removeSuffix("(partial)").trim())
  } else {
    Supported(since)
  }
}

private fun SupportInfo?.toSupportEntry(id: String, name: String, link: String): SupportEntry {
  val status = toSupportStatus()

  val note = if (this?.since == null) {
    "Know the status of this feature? [Let us know!](https://github.com/$repo/edit/main/data/projects/$id.json)"
  } else {
    note
  }

  val label = when(status) {
    NotApplicable -> "N/A"
    NotSupported -> "-"
    is Partial -> status.version
    is Supported -> status.version
    Unknown -> "?"
  }

  return SupportEntry(
    link,
    name,
    label,
    note?.let { Markdown(it) },
    status
  )
}

