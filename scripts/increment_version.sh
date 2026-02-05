#!/bin/bash

cd "$(dirname "$0")"
cd ..

old_version=$(scripts/get_version.sh)
parts=( ${old_version//./ } )
incremented=$((parts[2] + 1))
new_version="${parts[0]}.${parts[1]}.${incremented}"

filename='gradle.properties'
sed -i'.bak' "s/libraryVersion=$old_version/libraryVersion=$new_version/" $filename
rm $filename.bak

filename="README.md"
sed -i'.bak' "s/japanholidays:$old_version/japanholidays:$new_version/" $filename
rm $filename.bak

echo $new_version
