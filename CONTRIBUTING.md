# Contributing

Contributions are **strongly encouraged**! This project is only as useful as the data it contains, and we'd love your help keeping it accurate and complete.

## Adding data

The most impactful way to contribute is by adding or updating data. All data lives in the `data/` directory as simple JSON files.

### Add support for a feature in an existing project

Edit the project file in `data/projects/<project-id>.json` and add an entry to the `features` map:

```json
{
  "features": {
    "feature-id": {
      "since": "1.2.0"
    }
  }
}
```

The `feature-id` must match the filename (without `.json`) of an existing feature in `data/features/`.

Each feature entry supports the following fields:

- `since` — the first version that supports the feature or one of the special values:
  - `-` if not supported
  - `n/a` if not applicable 
  - `?` if unknown
- `note` (optional) — any clarification about the support (Markdown is supported).

### Add a new project

Create a new file `data/projects/<project-id>.json`:

```json
{
  "name": "My Project",
  "type": "client",
  "description": "A short description of the project.",
  "url": "https://github.com/org/my-project",
  "features": {}
}
```

`type` is typically `client`, `server`, `gateway`, `ide`, or `tool`.

Then add feature support entries as described above.

### Add a new feature

Create a new file `data/features/<feature-id>.json`:

```json
{
  "name": "Feature Name",
  "description": "A description of the feature.",
  "url": "https://link-to-spec-or-proposal"
}
```

- `description` — a plain-text description of the feature.
- `url` (optional) — a link to the relevant spec, proposal, or RFC.

Then add support entries for this feature in the relevant project files.

## Building the site locally

```bash
./gradlew buildWebsite
```

The generated site is in `build/site/`.

## Guidelines

- Keep descriptions concise.
- Use the project's official name.
- Link to the relevant spec or proposal via the feature's `url` field.
- When in doubt, open an issue or a draft PR — we're happy to help!
