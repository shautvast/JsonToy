# Json
a JSON serializer based on bytecode manipulation

* uses ASM for creating object mappers
* deserializing not (yet?) implemented
* write to String: Mapper.json(...)


* ~~as of java9 it needs `--add-opens java.base/java.lang=ALL-UNNAMED` as java commandline option.~~

* prerequisite: at least jdk9