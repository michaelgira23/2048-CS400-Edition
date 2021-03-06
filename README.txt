README

Course: CS400
Semester: Spring 2020
Project Name 2048-CS400 Edition
Team Members:
1. William Cong, X-Team 152, Lecture 001
2. Michael Gira, X-Team 251, Lecture 002
3. Faith Isaac, X-Team 213, Lecture 002
4. Quan Nguyen, X-Team 251, Lecture 002
5. Hanyuan Wu, X-Team 213, Lecture 002


Notes:

Known Bugs: When the player slides the tiles really fast (by pressing arrow keys or WASD), the game tile animations will sometimes flicker. However, this should not affect the actual state of the game after the animation is over.

Future Work: Depending on the version of 2048 you play, the game can extend even longer than reaching 2048 so it would be interesting to add formalized levels. We also want to add a dropdown with more game themes to choose from besides just binary (for example, hex or octal).

Concept: This game is a recreation of 2048 using JavaFX, seeing as there is not a fun enough, yet intellectually stimulating, game made with JavaFX. On a 4x4 playing board, the player can slide all the squares up, down, right, and left. If two squares with the same number slide into each other, they will combine into a single square with an incremented number. The player's goal is to combine the numbered squares until they reach 2048. They lose once the playing board is filled up and they can no longer combine any more squares.

Notes to the grader: We have placed a leaderboard.json and leaderboard-2.json with fabricated high scores because we are not nearly as good at playing the game as we are at programming it.

This project was created by A-Team 68 for CS400, Spring 2020 at the University of Wisconsin-Madison
