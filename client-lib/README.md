client-lib
==========

Non-executable library, to standardise communication between the server and clients. 

Client to Server
----------------

The enumeration CommandType contains all types of commands (including name, unique id and size of serialized command in chars)

The interface Command is implemented by all commands, requiring them to implement getCommandType() (which returns the CommandType)
and serialize(), which serializes the command in a standard way, to produce an array of char. This can then be sent over a network.

The class CommandParser is a thread-safe class, which can have a thread writing characters to it and a thread reading the parsed Commands from it.
The CommandParser will recieve characters (probably from an InputStream from a client) and, when it recieves the correct number of characters for the specified command type,
it will construct a new array and add it to a buffer until a second thread reads it.

All Command classes are tested using JUnit 4 (by test suite groupractical.tests.client.command.AllCommandTests).