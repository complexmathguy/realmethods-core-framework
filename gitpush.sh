#!/bin/bash

echo reset
git reset

echo init the repository
git init

git pull git@github.com:complexmathguy/realmethods.git

echo add all files from root dir below
git add pom.xml
git add *.bat
git add src/main/**/*
git add dependencies/**/*
git add docs/*


echo 'commit all the files'
git commit -m "first commit"
git remote add origin https://github.com/complexmathguy/realmethods.git
git push -u origin master


