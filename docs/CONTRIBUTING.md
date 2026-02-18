<!-- TOC -->
* [Contributing to YAGFI](#contributing-to-yagfi)
  * [Local Development Setup](#local-development-setup)
    * [Required Environment Variables](#required-environment-variables)
  * [Suggest labels](#suggest-labels)
  * [Frontend issues](#frontend-issues)
  * [Backend issues](#backend-issues)
* [Testing](#testing)
  * [Running tests](#running-tests)
  * [Integration tests](#integration-tests)
    * [Writing a new integration test](#writing-a-new-integration-test)
  * [Unit tests](#unit-tests)
    * [Structure](#structure)
    * [Naming](#naming)
    * [Assertions](#assertions)
    * [Test body](#test-body)
    * [Example](#example)
* [AI](#ai)
  * [Why fully AI-generated PRs without understanding are not helpful](#why-fully-ai-generated-prs-without-understanding-are-not-helpful)
<!-- TOC -->

# Contributing to YAGFI
The YAGFI project welcomes contributions from everyone. There are a number of ways you 
can help:

## Local Development Setup

### Required Environment Variables

Ensure the following environment variables are set before running the application:

| Variable        | Description                                      | How to obtain                                                               |
|-----------------|--------------------------------------------------|-----------------------------------------------------------------------------|
| `GithubBearer`  | GitHub personal access token for GraphQL API     | [Create token](https://github.com/settings/tokens) with `public_repo` scope |
| `IP_INFO_TOKEN` | ipinfo.io API token for IP-to-country resolution | [Sign up at ipinfo.io](https://ipinfo.io/signup) (Lite plan: free, unlimited requests, country-level data) |

## Suggest labels
Feel free to open an issue with *another one* custom good-first-issue label with example 
where it's used.
The list of current supported issues is [here](https://github.com/Regyl/yagfi-back/blob/master/src/main/resources/data/labels.txt)

## Frontend issues
Since I'm primarily a backend developer and only studying in frontend, I would be very 
grateful for any help with it.
If you even supply some explanations with your PR - I will marry you (joke (not sure)).

## Backend issues
Although I'm a good (subjectively) Java developer, I have not enough free time to implement 
everything I would like to see in this project.
So, if you found a good issue for you (or just my notes from README) - feel free to 
contact/open an issue and ask any questions if you have some.

# Testing

## Running tests
```bash
# Run all tests
mvn test

# Run only integration tests
mvn test -Dgroups=Integration

# Run only unit tests
mvn test -Dgroups=Unit
```

## Integration tests
Integration tests use [Testcontainers](https://java.testcontainers.org/) to spin up a real PostgreSQL 15.3
instance. Flyway migrations run automatically, so tests exercise actual SQL against the real schema.

Docker must be running on your machine for integration tests to work.

### Writing a new integration test
1. Place it in the same package as the class under test (e.g. `repository/` for repository tests).
2. Annotate the class with `@DefaultIntegrationTest`, `@SpringBootTest`, `@ActiveProfiles("test")`,
`@Testcontainers`, and `@Transactional`.
3. Use `@Nested` inner classes to group tests per method. This keeps the test class extensible as
more methods are covered.
4. Test classes must be **package-private** (no `public` modifier) per the project's checkstyle rules.

See `DataRepositoryTest` for a working example.

## Unit tests

Unit tests use Mockito for mocking. Key conventions:

### Structure
- Annotate with `@DefaultUnitTest` (bundles Mockito extension + strict stubs)
- Test classes must be **package-private** (checkstyle rule)
- Use `@Mock` for dependencies, `@InjectMocks` for the class under test
- Place test class in the same package as the class under test

### Naming
- **Test methods**: `methodName_condition_expectedBehavior`
  - e.g., `getCountry_clientReturnsValidResponse_returnsCountryString`
  - e.g., `getCountry_clientThrowsException_returnsNull`
- **Test class**: `{ClassUnderTest}Test`

### Assertions
- Use AssertJ fluent assertions (`assertThat`)
- One logical assertion per test; chained assertions on the same object are fine

### Test body
- Follow **Arrange / Act / Assert** structure, separated by blank lines — no explicit comments needed
- The test name and structure should be self-documenting

### Example
```java
@Test
void getCountry_clientReturnsValidResponse_returnsCountryString() {
    IpInfoResponseDto response = new IpInfoResponseDto();
    response.setCountry("US");
    when(ipInfoClient.getIpInfo("1.2.3.4")).thenReturn(response);

    String result = ipInfoService.getCountry("1.2.3.4");

    assertThat(result).isEqualTo("US");
}
```

See `IpInfoServiceImplTest` for a working example.

# AI
The era we just entered is AI-powered, and it's okay. The question is: do you primarily a developer, 
or a customer. Here at YAGFI, we are primarily developers who can use AI as a tool, not as 
a replacement for developers. According to the speech above, YAGFI following next policy:

- The PR author should **understand the core ideas** behind the implementation end-to-end, and
be able to justify the design and code during review.
- **Calls out unknowns and assumptions**. It’s okay to not fully understand some bits of AI generated code. 
You should comment on these cases and point them out to reviewers so that they can use their knowledge 
of the codebase to clear up any concerns. For example, you might comment “calling this function 
here seems to work but I’m not familiar with how it works internally, I wonder if there’s a race 
condition if it is called concurrently”.

## Why fully AI-generated PRs without understanding are not helpful
Today, AI tools cannot reliably make complex changes to DataFusion on their own, which is why we 
rely on pull requests and code review.

The purposes of code review are:

1. Finish the intended task.
2. Share knowledge between authors and reviewers, as a long-term investment in the project.

For this reason, even if someone familiar with the codebase can finish a task quickly, we’re still 
happy to help a new contributor work on it even if it takes longer.

An AI dump for an issue doesn’t meet these purposes. Maintainers could finish the task faster 
by using AI directly, and the submitters gain little knowledge if they act only as a pass 
through AI proxy without understanding.

Please understand the **reviewing capacity is very limited** for the project, so large PRs which 
appear to not have the requisite understanding might not get reviewed, and eventually 
closed or redirected.