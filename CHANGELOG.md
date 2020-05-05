# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.3.0]

### Added

- Button to delete images
- Swagger documentation

### Changed

- Icon when student answers changes between correct, wrong or uncorrected.



## [1.2.0] - 11/04/2020

### Added

- Button to flip relations on the unit diagram
- Icon shows when student has answered a question
- Itinerary tree keeps opened changing the tab or adding new blocks
- Tooltips in lesson editor tools
- All slides are updated when changing a lesson, card, unit or slide name
- Update name of the results of units search and focused units on save

### Fixed

- Questions do not disorder in student lessons
- Load content of lesson editor tools on init
- Update name of a unit tab when it changes
- Disallowed saving a unit with name containing the character "/"
- Disallowed saving units with the same name and a mutual parent
- Disallowed delete a unit if its name will cause conflicts

## [1.1.0] - 01/04/2020

### Added

- Tree-table added in the module progress
- Tab feature, divided into unit tabs and course tabs
- Tutorials and help notifications in unit diagram
- Confirm dialogs in all acctions that require saving or reloading data
- Save the state of the unit diagram even if you navigate elsewhere
- Student tabs feature, saving the lesson state
- Improved units diagram usability: added tooltips, responsive diagram size and clicks prevention while loading
- Student answers also depends on course and block

### Changed

- Images are now into a determined unit, so the import includes unitId

### Fixed

- Navigation to lessons from the course
- Adding lesson with cards and images now shows image, not include text
- Scroll independently in lesson editor
- When editing cards its not allowed to drag elements
- When editing card tittle, the item wont toggle
- Deleting lesson doesnt return error.

## [1.0.1] - 26/03/2020

### Fixed

- Load content of new units
- Edit and remove association relations
- Infinite recursion when you add an itinerary that contains itself
- Student role is allowed now to see question info.
- Student lesson view doesnt show Question button if there are no questions in the lesson.

## [1.0.0] - 09/03/2020

### Added

- Collapsible cards
- Cards can be reordered
- Auto-Paste when inserting in lesson editor

### Changed

- Definition question tables moved

### Fixed

- The lesson view dosn´t crash when you insert a slide in the same slide
- Lesson editor and Firefox compatibility
- Changed insert parameters order

## [0.1.0] - 19/02/2020

### Added

- Help tooltip when you add a module to a course
- Teacher can correct uncorrected questions
- Definition Questions tracking with pie chart, uncorrected and corrected answers
- List and Test Questions tracking with pie and bar charts
- Show multiple units at the same time
- Show the specified level of parents and children in the diagram
- Create separate units
- Show errors when there are units with invalid name
- Posibility to insert card and slides by name
- Ability to store images
- Insert images in slides
- Cards editor with asciidoc parser
- Answer questions inside a lesson
- Add questions to lesson
- Back security included
- Basic API Testing

### Fixed

- Dont show empty units adding a new course
- The lesson view dosn´t crash when an insert error happens
