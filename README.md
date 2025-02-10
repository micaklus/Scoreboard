# Live Football World Cup Scoreboard

## Overview
This is a Kotlin library implementing a live football scoreboard. It allows:
- Starting a new match with initial score 0-0
- Updating the score of an ongoing match
- Finishing a match and remove him from ongoing matches
- Retrieving a summary of ongoing matches, ordered by total score (and most recent if tied)


## Installation
Clone the repository and build the project. SDK code with tests is located lib module

## Assumptions for Tests

### **Match Summary**
- Return all started matches

### **Match Creation**
- Teams must have unique names: Starting a match with the same team names is not allowed.
- Start a new match with an initial score of 0-0: Matches are initialized with 0-0 scores and added to the summary list.
- Team names cannot be blank: Starting a match with blank or empty team names is not permitted.

### **Managing Matches**
- Finish a match and remove it from the scoreboard: Finishing a match removes it from the list of ongoing matches.
- Error when trying to finish a non-existing match: Attempting to finish a match that doesn't exist results in an error.
- A team can start a new match after finishing the previous one: Teams are eligible to join new matches after the previous one is concluded.
- Allow finishing a match without updating the score: Matches can be finished even if no score updates were applied.

### **Score Updates**
- Update match with new score
- Scores cannot be negative: Negative scores during updates are not allowed (e.g., `-4`).
- Updating scores with the same values is allowed: Scores can be updated to the same values multiple times without error.
- Cannot update scores for a non-existing match: Only existing matches can have their scores updated.
- Allow lowering scores with a valid reason: Scores can be decreased (e.g., via VAR decisions) with an explanation.
- Overall scores updated for all matches accurately: Match scores are validated after bulk updates to ensure accuracy across all.

### **Match Restrictions**
- No team can play two matches at the same time: Teams already playing cannot start another match until the previous one is finished.
- (Threading) Handle a large number of matches without issues: The scoreboard supports up to 1000 matches for stress testing.

### **Input validation**
- Reasons provided for lowering scores must not exceed 100 characters.
- Scores must be non-negative and cannot exceed a value of 2000.
- Team names must have a maximum length of 50 characters.

## Usage

Is described in documentation folder


## Steps in solving problem
1. Create multi-module project
2. Write assumptions
3. Generate fail tests for all assumptions
4. Run all test first time (all fails)
5. Written all tests and running them successfully
6. Threading tests and fixing
7. Documentation comments for usable methods and classes
