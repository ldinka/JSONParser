# JSONParser

JSONParser allows you to parse a json into a HashMap-object. So elements are parsed as follows:
* json-objects turn into HashMap-objects;
* json-arrays turn into ArrayList-objects;
* numbers turn into JNumber, which extends java.lang.Number. You can parse it by your own, taken a value via toString()-method;
* strings turn into String-objects;
* true/false turn into boolean-primitives;
* null turns into null.
