# software-architecture-TDT4240

# Authors:
* Nikolas Emil

# Game concept

The concept for our project is a mobile game called Inverse Pac-Man. It is similar to the original Pac-Man game, but with a twist: the player can play as the ghosts. 

The original Pac-Man was a “maze chase” arcade game developed by Namco. The player controls what is essentially an animated yellow circle through a dot-filled maze. The dots are called “pellets”, and Pac-Man must eat all of them in order to beat the level. At the same time, Pac-Man must avoid colliding with four colored ghosts that also roam the maze and pursue him. If Pac-Man collides with any of the ghosts, he dies and loses a life. If Pac-Man loses all of his lives, the game ends. Some pellets (called “power pellets”) are special and, when eaten, allow Pac-Man to also eat the ghosts, which causes them to respawn in the middle of the maze after some time. There are also fruits and other objects that sometimes appear in the maze that, when eaten, give the player extra points.

In singleplayer Inverse Pac-Man, the player can choose to play as Pac-Man or as the ghosts. Singleplayer as Pac-Man functions roughly the same as in the original game. With singleplayer as ghosts, the player can only control one ghost at a time, but can easily switch between them by tapping on the desired ghost. The remaining ghosts are controlled by AI. The goal is to prevent Pac-Man from eating all of the pellets in the maze. This can only be achieved by colliding into Pac-Man (assuming he has not recently eaten a power pellet), which causes him to lose a life. When Pac-Man loses all of his lives, the ghosts win! The quicker a player is able to defeat Pac-Man, the higher their score.

In multiplayer Inverse Pac-Man, players can play as either Pac-Man or as one of the ghosts. This means that a match can be up to 5 players: 1 Pac-Man and 4 ghosts. The objective for Pac-Man is the same as in the original game. The objective for the ghosts is the same as described above for singleplayer. In multiplayer, players will not be able to switch between ghosts; if a ghost does not have a player initially controlling it, it will always be controlled by AI. There will be separated leaderboards that keep track of how many wins a player has with each team.


# Git conventions
## Branches

* **master:** Updated only when milestones are completed
* **dev:** Updated each time a feature is finished (ideally working)
* **feat/feature-name:** A branch which is used to developed a feature
* **fix/feature-name:** A branch which is only used to fix a feature
* **hotfix:** A branch to fix major bugs discovered from a release to master


# Tutorials and other resources
* http://www2.cs.uidaho.edu/~jeffery/courses/328/lecture.html
* https://www.programcreek.com/java-api-examples/?code=kLeZ%2FLibGDX-MVC-Tutorial%2FLibGDX-MVC-Tutorial-master%2Fcore%2Fsrc%2Fcom%2Fmygdx%2Fgame%2Fmvctutorial%2Fmodel%2Fsystems%2FViewportResizeSystem.java
