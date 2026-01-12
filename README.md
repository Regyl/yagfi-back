<!-- TOC -->
* [Introduction](#introduction)
  * [Why yet another good-first-issue project?](#why-yet-another-good-first-issue-project)
  * [Need fix](#need-fix)
<!-- TOC -->

# Introduction
YAGFI - yet another good first issue

## Why yet another good-first-issue project?
First of all, when I searched for projects to contribute, I met one thing. 
Existing projects does not support all variety of labels:
- good-first-issue
- status: ideal-for-contribution
- beginner-friendly
- help wanted
and others.

The second reason was smart filtering. I'd like to contribute into live projects. 
Many of existing good-first-issue issues are abandoned.

To compare with, look for other similar projects:
- https://goodfirstissue.dev/
- https://goodfirstissues.com/
- http://goodfirstissue.com/
- https://www.goodfirstissue.org/
- https://forgoodfirstissue.github.com/
- https://up-for-grabs.net/

## Need fix
- supply each new query as task to task executor
- add insert on conflict do nothing
- add view and supply result to front from it. While one table is filling, other supply one hour older results
- remove all issues with label stale (idk how since github query does not support multiple label filters)