# MultiMapper v1.2

## Introduction

This simple application was written in Java 8.

Copyright © 2022 Christos Chartomatsidis

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/.

It uses the google gson library.
Besides this library, the rest is made by Christos Chartomatsidis, 2022.

This program may be freely distributed and used, but is handed as-is.
There is no copyright, and attribution is not required, but it would be really nice if given.

## Purpose

MultiMapper is a tool that generates simple, low-detail maps.
A Game-Master (GM) can use these to create locations and settings for his pen-and-paper RPG campaign / boardgame on the spot .
The stand-alone classes can also be implemented in a computer game, to procedurally create content (i.e. for roguelike games).

## Post-Production Note (added Jan 2023)

MultiMapper was my first personal project in OOP, when I was not yet familiar with many of the OOP-design principles, such as SRP, code maintainability, good and bad coding practices etc. Because of this, I made a lot of mistakes structure-wise, ending up with an application that is very very difficult to maintain and expand. This is the toll of self-teaching: making mistakes and learning (painfully) from them. Nevertheless, I am very proud of the end result, and I intend to re-create this idea from scratch in the future, only better.

## How to use

### How to run

In the folder MultiMapper.zip there is an executable .jar file and the 'lib' folder which contains the libraries needed. Just unzip in the same folder.
To run the application you need to have Java JRE installed in your system.

### Defining Parameters

There are two types of maps that can be generated: Landscape and Settlement.
Both of these can be set to a Theme; a texture set that produces a certain visual result. There are three themes for each map type.

In every type of parameter, there are maximum and minimum limits. If the user inserts a value beyond these limits, the application will automatically adjust it, or set it to some predetermined default.

#### General 

Both Landscape and Settlement map types share two common parameters: Number of Squares and Distortion.

Number of Squares is the number of squares in which the map is divided on the horizontal as well as on the vertical axis. Higher value means greater detail.
There are certain values which produce maps that do not 'fit' perfectly to the border. However, the map itself is canonical.

Distortion is an artificial 'noise' added to the squares of the grid, to avoid the tiles looking too 'square-y', especially in Landscape maps. Value of zero is perfect squares, while too high values may completely distort the map.

#### Landscape

Landscape Maps are created from top-to-bottom. 
The user may define the greatest elevation that a peak can have (Max Elevation), and the maximum number of peaks that may be created (but maybe there are less, in case the RNG causes them to overlap). The program expands these peaks downwards as slopes, until two slopes meet, or it reaches "sea level" (zero elevation).

The Themes for Landscape are: 

* Islands, a tropical-style archipelago, that can be expanded to a full-mass continent, if it is given enough peaks.
* Wasteland, an endless rocky mountainous desert.
* Alien Planet, a mysterious and unsettling terrain.
 
#### Settlement

Settlement Maps are created by creating horizontal streets which are connecting by vertical ones. Around every street, there are both buildings and empty plot tiles.
The user may define the number of horizontal streets, and the density of the buildings.
In Settlement maps there are three extra optional features: the presence of seashore, a river and a castle that surrounds the town. 
A lot of imperfections have been left on purpose, to make the map more believable- they can be attributed to poor city planning or cutting corners during the construction.

The Themes for Settlement are: 

* Village, medieval fantasy-style, surrounded by lush forest.
* Post-Apoc, after-the-nukes settlement where the buildings are made from rusted metal sheets, the fortifications are made from serrated scrap metal and all water is black from the radiation and the toxic waste.
* Dark Urban, a megalopolis of cold, glass skyscapers, in the middle of a gloomy nowhere.


### Menu: Import/Export

There is a checkbox named 'Show Gridlines'. While it doesn't have any effect on what the user sees on-screen, it affects the exported image of the map.

#### as .JPG

This saves the map to a simple jpg image, located in the folder from where the application is running.

#### as .PNG

Same as with jpg, but the background tiles are transparent.

#### to-and-from .JSON

Saves the raw map data in a json file, which can be loaded at a later time.
In order to properly load a json file, note the following:
The json must be in the folder from where the application is running.
Landscape map names MUST begin with "LAND-" and Settlement map names MUST begin with "TOWN-". Removing these prefixes will result in the map becoming unloadable.


## Technical Support

None. I do not intend to continue developing or working on this particular project. However, if you have comments or remarks, feel welcome to drop me a line.
