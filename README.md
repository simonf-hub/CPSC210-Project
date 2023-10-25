# Game: Dynamite Monster Kill


*What* will the application do? </p>
- Players will be able to control their character moving in x and y direction, when character hit Space_Key, then the dynamite randomly landed
  on the terminal will explode and kill monsters around the dynamite.

*Who* will use it? </p>
- Who like the game and want to be a player. </p>

*Why* is the project interested me? </p>
- Because I'm interested in becoming a game programmer or developer which I suppose this project is a good chance to
  practice and gain some real experience in developing game. </p>

## User story (Phase 1)
- As a user, I will be able to move my character in x and y direction.
- As a user, I want to kill monster. (lightUpBombs near monsters would kill them) (move a monster from the monsters)
- As a user, I can see the score goes up while I kill one monster.
- As a user, I want to light up the bomb. (move a bomb from the bombs)

## User story (Phase 2)
- As a user, I want to be able to save the current game (terminalKey: escape -> consoleTypeIn: a)
- As a user, I want to be able to load the last saved game (terminalKey: escape -> consoleTypeIn: s)

## Some notes
- Use space_key to light bombs
- When exit the game, hit X-key on the keyBoard will exit the game and print out EventLog

##Phase4: Task2

- The eventLog will print out after exit the game with X-key
- when character move, first eventLog occurs
- when character kills a monster, second eventLog occurs
- when character score a point, third eventLog occurs
- when character light up a bomb, fourth eventLog occurs

Thu Mar 31 16:41:05 PDT 2022
Character moved to: 470,425


Thu Mar 31 16:41:05 PDT 2022
You killed a monster at: 512,451


Thu Mar 31 16:41:05 PDT 2022
Your score is: 1


Thu Mar 31 16:41:05 PDT 2022
Character lighted the surrounding bombs


##Phase4: Task3
If I can have more time, I would apply the observer pattern into my design. In my project, I can make ScorePanel class implements
Observer and Game class extends Observable. Overriding the update in ScorePanel.
I can add an Observer in Game's constructor and have setChange() and notifyObserver() calls inside Game class whenever my changing variables change.
Therefore, the ScorePanel can update as game runs and doesn't need a Game parameter.

