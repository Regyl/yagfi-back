

- [Contributing to YAGFI](#contributing-to-yagfi)
  - [Suggest labels](#suggest-labels)
  - [Frontend issues](#frontend-issues)
  - [Backend issues](#backend-issues)
- [AI](#ai)
  - [Why fully AI-generated PRs without understanding are not helpful](#why-fully-ai-generated-prs-without-understanding-are-not-helpful)



# Contributing to YAGFI

The YAGFI project welcomes contributions from everyone. There are a number of ways you 
can help:

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