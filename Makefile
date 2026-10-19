.PHONY: run compile clean

run: compile
	mvn -q exec:java -Dexec.mainClass=zerql.Main

compile:
	mvn -q compile

clean:
	mvn -q clean
