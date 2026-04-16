package caniuse

import kotlinx.html.HTMLTag
import kotlinx.html.MAIN
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.footer
import kotlinx.html.h4
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.stream.createHTML
import kotlinx.html.title
import kotlinx.html.unsafe

internal val markdownRenderer = MarkdownRenderer()
internal val repo = "martinbonnin/caniuse"

internal fun HTMLTag.markdown(markdown: String) {
  unsafe {
    +markdownRenderer.render(markdown)
  }
}

internal fun generatePage(pathPrefix: String = "", content: MAIN.() -> Unit): String {
  return createHTML().html {
    head {
      title("Can I Use ... in GraphQL?")
      link(rel = "icon", type = "image/svg+xml", href = "${pathPrefix}caniuse.svg")
      link(rel = "stylesheet", href = "${pathPrefix}style.css")
      script(src = "${pathPrefix}theme.js") {}
    }
    body {
      header {
        div {
          id = "header-title"
          a(href = "${pathPrefix}index.html") {
            img(src = "${pathPrefix}logo.svg", alt = "Logo") {
              height = "30"
            }
          }
        }
        div {
          id = "header-actions"
          a(href = "https://github.com/$repo") {
            attributes["aria-label"] = "GitHub"
            attributes["title"] = "GitHub"
            classes = setOf("header-icon-link")
            img(src = "${pathPrefix}github.svg", alt = "GitHub") {
              width = "16"
              height = "16"
            }
          }
          a(href = "https://discord.graphql.org") {
            attributes["aria-label"] = "Discord"
            attributes["title"] = "Discord"
            classes = setOf("header-icon-link")
            img(src = "${pathPrefix}discord.svg", alt = "Discord") {
              width = "16"
              height = "16"
            }
          }
          div {
            id = "theme-switch"
            button {
              id = "theme-trigger"
              attributes["aria-label"] = "Change theme"
              attributes["title"] = "Change theme"
              // Icon inserted by JS
            }
            div {
              id = "theme-menu"
              // Items inserted by JS
            }
          }
        }
      }
      main {
        content()
      }
      footer {
        div {
          id = "footer-links"
          div {
            classes = setOf("footer-column")
            h4 { +"Resources" }
            a(href = "https://graphql.org") { +"graphql.org" }
            a(href = "https://spec.graphql.org") { +"GraphQL Specification" }
            a(href = "https://graphql.org/community/") { +"Community" }
          }
          div {
            classes = setOf("footer-column")
            h4 { +"Contribute" }
            a(href = "https://github.com/$repo") { +"GitHub Repository" }
            a(href = "https://github.com/$repo/blob/main/CONTRIBUTING.md") { +"Contributing Guide" }
            a(href = "https://github.com/$repo/issues") { +"Report an Issue" }
          }
          div {
            classes = setOf("footer-column")
            h4 { +"Connect" }
            a(href = "https://discord.graphql.org") { +"GraphQL Discord" }
            a(href = "https://graphql.org/foundation/") { +"GraphQL Foundation" }
          }
        }
        p {
          id = "footer-copyright"
          +"© 2026 GraphQL contributors"
        }
      }
    }
  }
}
