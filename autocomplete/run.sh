#!/bin/sh

## Determine the dictionary file

## Is the java version valid
is_version_valid() {
if [[ "$1" ]]; then
    version=$("$1" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    if [[ "$version" < "16" ]]; then
	echo "The minimum java version required to run the application is '16.x.x'.  The available version is '${version}'. Please install and/or update path or JAVA_HOME."
    fi
fi
}



if [ -z "$1" ]
then
   echo "Defaulting dictionary file to 'src/main/resources/scrabble.txt'."
   file="src/main/resources/scrabble.txt"
else
   file=$1
fi

## Find the java binary

java_bin=$(type -p java)

# Check to see if the java binary is in the path and that it's version is valid 
if [[ ! -z "$java_bin" ]] && [[ -z  $(is_version_valid $java_bin ) ]]; then
    _java=java
# Check to see if the JAVA_HOME is set and it's version is valid
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]]  && [[ -z $(is_version_valid $JAVA_HOME/bin/java) ]];  then
    _java="$JAVA_HOME/bin/java"
# Not found
else
    echo "A valid java installation or version is not found. The minimum version is '16'." 
    exit 1
fi

$_java -jar target/autocomplete-0.0.1-SNAPSHOT.jar --spring.dictionary.file=$file
