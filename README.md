# Tank
This project is for my Fundamental Java Programming assignment. Use Swing GUI as the course requires.

The project is organized as follows:
1) src/images: all image files that the game uses.
2) src/sounds: all sound files that the game uses.
3) src/maps/maps.txt: the terrain information of 10 levels.
4) src/zhou/kunpeng/tank: source code.


Gameplay:
Use W/A or UP/DOWN to select in the welcome page.
Use W/A/S/D or UP/DOWN/LEFT/RIGHT to control your tank.
Use F/Space to fire.
Use P/Esc to pause and resume.

If played with your partner, player 1 has to start the game first and determine the port.
Player 2 should start after that, and connect to Player 1's network address and port.

Package structure:

battle: All the classes mainly used in battle.
    GameMap: core of battle which stores the terrain information, and it is the parent container of the entire map as well.
    Tank (abstract): tank on the map. Provides a list of actions that a tank can do (e.g. move, stop and fire).
    Cannon: the cannon that the tank launches. Triggers tank hits and terrain destruction.

    PlayerTank: tank of player. It can revive!
    EnemyTank (abstract): tank of enemies. Updates information when an enemy tank is destroyed.
    PlayerKeyListener / ClientKeyListener: listen to user's key input. The former is for P1 (server) and the latter is for P2(client).

    Base: the base on the bottom of the map. When hit, the game is over.
    GameOverSign: the sign shown when losing the game.
    InfoPanel: shows information about the battle (scores, enemies left, player's life remaining, etc.)
    PauseScreen: the sign shown when the game is paused.
    MapUtils: some auxiliary static methods.

    battle.ai: Controls the behavior of enemy (AIOperator) and enemy creation (AIEnemyOperationOperator).
        AIOperator: controls the behavior of enemies.
        AIEnemyCreationOperator: determines when and where to create enemy tanks.
        EnemyCreator: the real worker who creates enemy tanks.
        Star: an image shown when an enemy is about to be created.

    battle.enemies: 4 types of enemy tanks.
        NormalTank: slow and fragile tanks.
        SecondaryTank: same as NormalTank except for a higher score.
        MobileTank: fast tanks.
        ToughTank: strong enemies that requires 3 hits to destroy.

comm: classes for network socket communication.
    NetComm (abstract): network sender and receiver.
    ServerNetComm / ClientNetComm: exact NetComm used by p1(server) and p2(client).
    NetListener (interface): interpret the message received by NetComm.
    Message (interface): format the messages to send.
    ByteUtil: some auxiliary methods related to byte[] array.

display: basic display components.
    ImageComponent: a component to display image.
    Clip: a component to display image sequences one by one as time passes.
    GoudyStoutFont: a font that many JLabels uses in game.
    Background: a component with only one color.

messages: classes dealing with network messages. Classes are in pair of comm.Message and comm.NetListener.

states: display states (i.e. Content Panes of JFrame).
    WelcomeState: first page with title "TANK".
    ConfigNetCommState: make connection.
    PrepareLevelState: shows level info before a level starts.
    BattleState: the state where game is played.
    ScoreCounterState: shows the scores after a level.
    HighestScoreState: shows highest scores.
    (logic: Welcome -> [ConfigNetComm] -> PrepareLevel / Battle / ScoreCounter (loop) -> HighestScore)

timer: a timeline simulator.
    Timeline: listen to a TimerEvent, and invokes all TimerListeners every frame.
    TimerListeners (interface): invoked when displaying a new frame.

Levels: keeps level terrain info.
PlayerState: keeps player scores, life and enemy tank destroyed.
Sound: play sound.
MainFrame: main JFrame. Maintaining a Timeline and a NetComm.
Main: entrance of application.

