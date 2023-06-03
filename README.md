# JsonToy
a JSON serializer based on bytecode manipulation

* creates a Json serializer for a java type using javassist
* deserializing not yet implemented
* see the unit tests to see how it works


* as of java9 it needs `--add-opens java.base/java.lang=ALL-UNNAMED` as java commandline option.