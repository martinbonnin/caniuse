package caniuse

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.span
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr

internal fun FlowContent.appendTable(columnName: String, entries: List<SupportEntry>) {
  table {
    classes = setOf("support-table")
    thead {
      tr {
        th { +columnName }
        th { +"Supported Since" }
        th { +"Note" }
      }
    }
    tbody {
      entries.forEach {
        tr {
          if (it.status is NotApplicable) {
            classes = setOf("not-applicable")
          } else if (it.status is Unknown) {
            classes = setOf("unknown")
          }
          td {
            a(href = it.link) {
              +it.name
            }
            if (it.experimental) {
              span {
                classes = setOf("badge-experimental")
                attributes["data-tooltip"] = "This feature has not been merged in a specification draft yet"
                attributes["tabindex"] = "0"
                attributes["role"] = "button"
                attributes["aria-label"] = "Experimental: This feature has not been merged in a specification draft yet"
                +"E"
              }
            }
          }
          td {
            classes = setOf(
              when(it.status) {
                is Unknown ->  "support-unknown"
                is NotApplicable ->  "support-unknown"
                is Supported -> "support-yes"
                is Partial -> "support-partial"
                else -> "support-no"
              }
            )
            +it.label
          }
          td {
            if (it.note != null) {
              markdown(it.note.value)
            }
          }
        }
      }
    }
  }
}