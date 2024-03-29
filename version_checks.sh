#!/usr/bin/env bash

declared_version=$(grep "version = " ./sincmathsmp/build.gradle.kts | sed 's/version = //' | sed 's/"//g')
citation_version=$(grep "version: " ./CITATION.cff | tail -1 | sed 's/version: //')
readme_version=$(grep 'implementation("io.github.gallvp:sincmathsmp:' ./README.md | sed 's/implementation("io.github.gallvp:sincmathsmp://' | sed 's/")//')

if [[ "$citation_version" != "$declared_version" ]];then
    echo "citation_version ($citation_version) is not the same as declared_version ($declared_version) version"
    exit 1
fi

if [[ "$readme_version" != "$declared_version" ]];then
    echo "readme_version ($readme_version) is not the same as declared_version ($declared_version) version"
    exit 1
fi
