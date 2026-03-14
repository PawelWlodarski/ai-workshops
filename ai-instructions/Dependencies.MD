# AI Instructions

- For any request involving the **latest version** of a dependency, plugin, framework, or tool, **do not answer from memory**.
- Always verify using a **live source**.
- For Java/Kotlin dependencies, prefer **Maven Central / Sonatype Central** unless the user requests another source.
- Return the **latest stable** release only, unless the user explicitly asks for beta, RC, milestone, snapshot, or preview versions.
- Include:
  - exact artifact coordinates
  - exact version
  - dependency snippet
  - verification link
- If live verification is unavailable, say so clearly and **do not guess**.
