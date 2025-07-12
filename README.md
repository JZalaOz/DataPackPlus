<br>
<div align="center">
    <a href="https://github.com/JZalaOz/DataPackPlus">
        <img src="src/main/resources/assets/datapackplus/icon.png" alt="Logo" width=80 height=80>
    </a>
    <h3 align="center">DataPackPlus (DP+)</h3>
    <p align="center">
        A helpful Minecraft Fabric mod to improve the lives of data pack developers!
        <br />
        <br />
        <a href="https://github.com/JZalaOz/DataPackPlus/issues/new?template=bug-report--.md">Report Bug</a>
        &middot;
        <a href="https://github.com/JZalaOz/DataPackPlus/issues/new?template=feature-request--.md">Request Feature</a>
    </p>
</div>

## About the project

My friend Nedraw is a datapack developer, and we're working together on modding and datapack development. To help him out, I made an api for him, so he can make datapacks easier and with better performance.

I decided to open-source and publish this so I can help other people out.

<hr>

Here are the features this mod adds.

* `/data math` <br>

This command allows you to do more complex and faster math operations. <br>
Here are some examples on how to use the command:<br>

```
/data math <operation> with <input_nbt> (Will just show the result in chat)
/data math <operation> storage <storage_identifier>
/data math <operation> storage <storage_identifier> <nbt_subpath>
```

* `/data variable` <br>

This command allows you to set and get specific variables for an entity.
Here are some examples on how to use the command:<br>

```
/data variable <add|set|get> <variable> (Will just show the result in chat if get)
/data variable <add|set|get> <variable> to <storage_identifier>
/data variable <add|set|get> <variable> to <storage_identifier> <nbt_subpath>
```

* `Ridable players`<br>

You can have entities mount and dismount onto players; the player being ridden cannot force the entity to dismount.

Example of using /data math (the NBT schema is the same for variable)
```
/data modify storage minecraft:test in set value [43,35] (Storage nbt: {in:[43,35]} )
/data math add storage minecraft:test (Storage nbt: {in:[43,35],out:[78.0d]})
```

## Math operations

#### Arithmetic Operations

For all arithmetic operations, the input is an array of numbers of any size. If the size is more than 2, it will treat it as chaining the operations, for example, if it's an add operation with 4 inputs, it will sum it all up.

* add
* subtract
* multiply
* divide

<br><hr>

### Math Operations
If you want to know what these operations are, just search for them.

| Operation Name         | Input Size | Notes                     |
|------------------------|:----------:|:-------------------------:|
| add                    | 2          |                           |
| subtract               | 2          |                           |
| multiply               | 2          |                           |
| divide                 | 2          |                           |
| mod (modulo)           | 2          |                           |
| negate                 | 1          |                           |
| factorial              | 1          |                           |
| power (exponentiation) | 2          |                           |
| root (nth root)        | 2          |                           |
| distance               | 2          | input: [x,y,z], [x,y,z]   |
| distance_squared       | 2          | input: [x,y,z], [x,y,z]   |
| rad2deg (radians→deg)  | 1          |                           |
| deg2rad (degrees→rad)  | 1          |                           |
| sin                    | 1          |                           |
| cos                    | 1          |                           |
| tan                    | 1          |                           |
| arcsin                 | 1          |                           |
| arccos                 | 1          |                           |
| arctan                 | 1          |                           |
| arctan2                | 2          |                           |
| ceil (round up)        | 1          |                           |
| round                  | 1          |                           |
| floor (round down)     | 1          |                           |

### Variable Types

| Variable Name | Description                                              | Type                                    | Accessors     |
|---------------|----------------------------------------------------------|-----------------------------------------|---------------|
| motion        | The entity's motion/velocity                             | [double, double, double]                | Get, Set, Add |
| hunger        | The entity's foodLevel/Hunger and Saturation             | [double (hunger), double (saturation) ] | Get, Set, Add |
| health        | The entity's health                                      | [double (0 to 20.0) ]                   | Get, Set, Add |
| held_slot     | The player's selected hotbar slot                        | [int (0 to 8) ]                         | Get, Set, Add |
| fall_distance | How far the entity has fallen, 0 if they are not falling | [double]                                | Get           |
| dimension     | The entity's dimension/world with namespace              | [string]                                | Get           |
| position      | The entity's xyz position                                | [double,double,double]                  | Get, Set, Add |
| angle         | The entity's yaw and pitch angle                         | [double (yaw), double (pitch) ]         | Get, Set, Add |