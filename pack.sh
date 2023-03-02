#!/usr/bin/env bash

if [[ -z "$1" ]] || [[ ! $1 =~ ^[0-9]+$ ]]; then
    echo "Invalid assignment '$1'"
    exit 1
fi

asgn_dir="asgn$1"

[[ ! -d "$asgn_dir" ]] && { echo "'$asgn_dir': not a folder"; exit 1; }

branch="$(git symbolic-ref -q --short HEAD || git describe --tags --exact-match)"
git archive --format=zip -o "CS102_Sec2_Asgn${1}_Dag_Umut.zip" "$branch" "$asgn_dir"

