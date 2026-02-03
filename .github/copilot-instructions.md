# YAGFI Backend - AI Coding Guidelines

## Project Purpose
YAGFI (Yet Another Good First Issue) is a backend service that aggregates open-source GitHub issues suitable for new contributors. The platform queries GitHub's GraphQL API every 12 minutes, filters issues by configurable labels (good-first-issue, help-wanted, beginner-friendly, etc.), and provides REST/GraphQL endpoints for filtering and discovering issues.

## Architecture Overview

### Core Components
- **Data Ingestion Pipeline**: `IssueLoaderServiceImpl` orchestrates scheduled pulls from GitHub via `IssueSourceService` implementations
- **Issue Storage**: Two-table switching pattern (`e_issue_1`/`e_issue_2` + `e_repository_1`/`e_repository_2`) for zero-downtime updates using database views (`issue_v`, `repository_v`)
- **Data Access**: `DataService` provides query APIs; `DataController` exposes REST endpoints (`/api/issues`)
- **GitHub Integration**: `GraphQlClientFactory` creates `GraphQlClient` beans using Spring's HttpSyncGraphQlClient with bearer token auth

### Technology Stack
- **Framework**: Spring Boot 4.0.0 with Spring Cloud 2025.1.0
- **Database**: PostgreSQL with Flyway migrations (schema: `gfi`)
- **Data Access**: MyBatis (XML mappers in `src/main/resources/mapper/`)
- **Concurrency**: Custom async executors configured in `AsyncConfiguration` (core: 20, max: 100 threads)
- **Caching**: Spring Cache abstraction (languages list cached)
- **External API**: GitHub GraphQL via Spring's GraphQL client

## Key Patterns & Conventions

### Zero-Downtime Deployments
The system uses a **dual-table pattern** to avoid downtime during data updates:
- Tables exist in pairs: `e_repository_1/e_repository_2` and `e_issue_1/e_issue_2`
- Database views point to the active table: `issue_v` and `repository_v`
- `determineTable()` in `IssueLoaderServiceImpl` switches which pair is active
- `replaceView()` atomically updates views after loading completes

**When modifying schema**: Update both paired tables in Flyway migrations (V1__...).

### Async Processing with Custom Executors
- `@Async` methods use default executor OR named bean `@Qualifier("issueLoadAsyncExecutor")`
- `IssueLoaderServiceImpl.schedule()` uses `LockSupport.parkNanos()` to wait for queue to empty
- Issue loading in `DataServiceImpl.save()` is async to avoid blocking request threads

**When adding async work**: Use the named executor for long-running I/O; default for quick tasks.

### Configuration & Properties
- GitHub token: `${GithubBearer}` (environment variable required)
- Auto-upload enabled: `spring.properties.auto-upload.enabled` (default: true)
- Scheduled frequency: `spring.properties.auto-upload.period-mills` (default: 600000ms = 10 min, docs say 12 min)
- Database schema: `${spring.properties.db.schema}` (default: `gfi`)
- Thread pools: `core-pool-size` (20) and `max-pool-size` (100) configurable

### MyBatis Integration
- Type handlers: Register custom types in `src/main/java/com/github/regyl/gfi/typehandler/`
- Mappers: XML files in `src/main/resources/mapper/` (e.g., `DataRepository.xml` maps `DataRepository` interface)
- Aliases: DTOs auto-aliased via `mybatis.type-aliases-package`

### Service Mappers as Function Beans
`DataServiceImpl` injects mappers as function beans:
```java
BiFunction<Map<String, RepositoryEntity>, GithubIssueDto, IssueEntity> issueMapper;
Function<GithubRepositoryDto, RepositoryEntity> repoMapper;
```
These are wired via `@Component`-annotated implementations in `mapper/` package. Use this pattern for declarative data transformations.

## Typical Workflows

### Adding a New Issue Label
1. Update `src/main/resources/data/labels.txt` with the label name
2. Modify the GitHub GraphQL query in `src/main/resources/graphql/github-issue-request.graphql` if needed
3. Label filtering occurs in `IssueSourceService` implementations (query builder logic)

### Querying Issues
```
POST /api/issues
Body: DataRequestDto (filters like language, labels, repository)
Returns: DataResponseDto with paginated IssueResponseDto list
```

### Adding Database Migrations
- Create `V{N}__Description.sql` in `src/main/resources/db/migration/`
- Flyway validates on startup; check logs if migration fails
- For schema changes: update **both** paired tables and views

### Local Development
- Requires PostgreSQL running with `gfi` schema
- Set environment variables: `GithubBearer` (GitHub API token), `APP_HOST_NAME` (database host)
- Application runs on `http://localhost:8080/api` with CORS enabled (temporary, see `WebConfiguration.java` TODO)

## Important Notes

- **CORS Configuration**: Currently permissive (`addMapping("/**").allowedMethods("*")`). Address before production.
- **Rate Limits**: GitHub GraphQL has higher rate limits than REST API; use GraphQL for bulk fetches.
- **View Replacement**: The `replaceView()` call after loading is critical—missing it leaves consumers on stale data.
- **Timeout on Async Tasks**: `IssueLoaderServiceImpl` has a FIXME comment about lacking timeouts; consider adding circuit-breaker logic if tasks hang.
- **TypeHandler Pattern**: Custom type handlers (e.g., `StringListTypeHandler`) handle PostgreSQL array → Java type conversion.

## Code Style
- Lombok annotations for boilerplate (`@RequiredArgsConstructor`, `@Slf4j`)
- Immutable DTOs with validation (`@Valid` annotations in controllers)
- Streams & functional programming for collections manipulation
- All new code must include Javadoc or clear logging for debugging

## Common Gotchas
- **Foreign Key Constraints**: Deleting a repository cascades issue deletion; design safe cleanup.
- **Stale Cache**: Languages are cached; after label updates, manual cache eviction may be needed.
- **Thread Pool Saturation**: 100 max threads for issue loading; monitor queue size during high concurrency.
