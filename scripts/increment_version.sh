#!/bin/bash

cd "$(dirname "$0")"
cd ..

old_version=$(scripts/get_version.sh)
parts=( ${old_version//./ } )
incremented=$((parts[2] + 1))
new_version="${parts[0]}.${parts[1]}.${incremented}"

sed -i '' "s/libraryVersion=$old_version/libraryVersion=$new_version/" gradle.properties

echo $new_version
