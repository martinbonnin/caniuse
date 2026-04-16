# Contributing

Contributions are **strongly encouraged**! This project is only as useful as the data it contains, and we'd love your help keeping it accurate and complete.

## Adding data

The most impactful way to contribute is by adding or updating data. All data lives in the `data/` directory as simple JSON files.

### Add support for a feature in an existing project

Edit the feature file in `data/features/<feature-id>.json` and add an entry to the `projects` map:

```json
{
  "projects": {
    "your-project-id": {
      "since": "1.2.0"
    }
  }
}
```

- `since` — the first version that supports the feature.
- `note` (optional) — any clarification about the support.
- `relevant` (optional) — set to `false` if the feature doesn't apply to this project (e.g. a transport-level feature for a transport-agnostic library).

### Add a new project

Create a new file `data/projects/<project-id>.json`:

```json
{
  "name": "My Project",
  "type": "client",
  "description": "A short description of the project.",
  "url": "https://github.com/org/my-project"
}
```

`type` is typically `client`, `server`, or `tool`.

### Add a new feature

Create a new file `data/features/<feature-id>.json`:

```json
{
  "name": "Feature Name",
  "description": "A description of the feature. Markdown is supported.",
  "projects": {}
}
```

Then add project support entries as described above.

## Building the site locally

```bash
./gradlew buildWebsite
```

The generated site is in `build/site/`.

## Guidelines

- Keep descriptions concise.
- Use the project's official name.
- Link to the relevant spec or proposal in feature descriptions.
- When in doubt, open an issue or a draft PR — we're happy to help!
