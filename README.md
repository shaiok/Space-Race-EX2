# Space-Race-EX2
Space Race
Overview
Space Race is an exhilarating space-themed game that challenges players to navigate through a cosmic obstacle course. With customizable settings, multiple game modes, and exciting features, players can experience an out-of-this-world gaming adventure on their mobile devices.

Specifics
Adjustable number of rows  is 8  and 5 columns.
Obstacles come at a constant speed in space (obstacles come towards the player).
Crash notification - toast message + vibration + sound.
3 Lives - Game over in 3 lives.
App icon design by picture & background color.
3 Game modes: buttons-fast, buttons-slow, sensors.
Sensor mode - contains the ability to tilt right/left to move the player.
Sensor mode - contains the ability to tilt up/down to change game speed.
Score - Each successful navigation through obstacles earns points, each power-up collected earns bonus points.
New Implementations
Using Fragments - one fragment for the player's records and one for a map: a click on a specific record will show the player's location on the map.
Using SharedPreferences - the player's records are stored on the device's SharedPreferences.
Using Google Maps API - fetching Google Maps.
Using Device's location services - for setting the longitude and altitude of the player's current location.
Using MediaPlayer - Used for crash sound ('Ouch'). and coin sound 
Using Sensors - in sensor mode game, tilt right/left to move the player & tilt up/down to change the speed of the game.
Game Flow
OnPause: Freezes when exiting the app.
OnResume: Continues from where it paused when returning to the app.
Timer: Scheduled appearance of obstacles.
Vibrator & Toast & Sound: Used for every crash.

