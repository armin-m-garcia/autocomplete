#!/bin/sh

## Is the java version valid
is_version_valid() {
if [[ "$1" ]]; then
    version=$("$1" -version 2>&1 | awk -F ' ' '{print $1}')
    if [[ "$version" < "16" ]]; then
	echo "The minimum java version required to run the application is '16.x.x'.  The available version is '${version}'. Please install and/or update path or JAVA_HOME."
    fi
fi
}

## Check for Maven

maven_bin=$(type -p mvn)

# Check to see if the maven binary is in the path 
if [[ ! -z "$maven_bin" ]]; then
    maven=mvn
# Not found
else
    echo "Maven is not found.  Please install maven and update PATH." 
    exit 1
fi

## Check for JDK >= 16 
javac_bin=$(type -p javac)

# Check to see if the javac binary is in the path 
if [[ ! -z "$javac_bin" ]] && [[ -z  $(is_version_valid $javac_bin ) ]]; then
    javac=javac
# Not found
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/javac" ]] && [[ -z $(is_version_valid $JAVA_HOME/bin/javac) ]];  then
    javac="$JAVA_HOME/bin/javac"
# Not found
else
    echo "The JDK with version >= 16 is not found.  Please install the JDK and update PATH or JAVA_HOME." 
    exit 1
fi



mvn clean install
 
