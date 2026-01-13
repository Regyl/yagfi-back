<!-- TOC -->
* [Introduction](#introduction)
* [Why yet another good-first-issue project?](#why-yet-another-good-first-issue-project)
* [Need fix](#need-fix)
* [What's next](#whats-next)
<!-- TOC -->

# Introduction
YAGFI - yet another good first issue

# Why yet another good-first-issue project?
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

# Implementation
Updates data every 12 minutes since GitHub rate limit allows no more

# Need fix
- supply each new query as task to task executor
- add view and supply result to front from it. While one table is filling, other supply one hour older results
- remove all issues with label stale by filtering the response since it contains labels

# What's next
- Android app with notifications about new issues by favorite filter preset
- AI to looking for new labels that might be another custom form of good-first-issue