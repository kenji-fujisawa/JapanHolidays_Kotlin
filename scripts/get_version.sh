#!/bin/bash

cd "$(dirname "$0")"
cd ..

./gradlew properties | grep "libraryVersion:" | awk '{print $2}'
